package com.powerspace.bidding

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpRequest}
import akka.http.scaladsl.server.Directives.{as, complete, entity, path, post}
import akka.http.scaladsl.unmarshalling.{FromRequestUnmarshaller, Unmarshaller}
import akka.stream.{ActorMaterializer, Materializer}
import cats.data.OptionT
import com.google.openrtb.{BidRequest, BidResponse}
import io.circe.Json
import monix.eval.Task
import monix.execution.Scheduler

import scala.concurrent.duration.Duration
import scala.concurrent.{ExecutionContext, Future}

object BidderApp extends App {

  import com.powerspace.openrtb.json.OpenRtbSerdeModule._

  implicit private val scheduler: Scheduler = ???
  implicit private val system: ActorSystem = ???
  implicit private val materializer: ActorMaterializer = ???

  implicit private val bidRequestUnmarshaller: Unmarshaller[HttpRequest, BidRequest] =
    new FromRequestUnmarshaller[BidRequest]() {
      override def apply(value: HttpRequest)(implicit c: ExecutionContext, m: Materializer): Future[BidRequest] =
        Unmarshaller.stringUnmarshaller(value.entity)
          .map(implicit json => bidRequestDecoder.decodeJson(Json(json)).right.get)
    }

  val bidder = new RtbBidder[Task]()

  val router =
    path("bidOn") {
      post {
        entity(as[BidRequest]) {
          bidRequest =>
            val bidResponse = bidder.bidOn(bidRequest)
              .runSyncUnsafe(Duration.Inf)
              .getOrElse(BidResponse.defaultInstance)

            complete(HttpEntity(ContentTypes.`application/json`, bidResponseEncoder(bidResponse).toString))
        }
      }
    }

  Http().bindAndHandle(router, "0.0.0.0", 8080)

  sys.addShutdownHook {
    system.terminate()
  }

}

class RtbBidder[F[_]] extends Bidder[F] {

  import scala.concurrent.duration._

  override def bidOn(bidRequest: BidRequest): F[Option[BidResponse]] = {

    val responses: Seq[F[Option[BidResponse]]] = bidRequest.imp.map(imp => scatterGather(bidRequest, imp))

    Task.gatherUnordered(responses)
      .map(_.flatten)
      .map(maybeResponses => {
        Option(maybeResponses
          .reduceOption(???)
          .getOrElse(???))
      })
  }

  private def scatterGather(request: BidRequest, imp: BidRequest.Imp): F[Option[BidResponse]] = {
    Task {
      doBid
        .timeout(request.tmax.getOrElse(50) millis)
        .flatMap(response => addBidExtension(response))
      }
  }

  private def doBid: Task[Option[BidResponse]] = {
    Task(Some(BidResponse.defaultInstance))
  }

  private def addBidExtension(bidResponse: Option[BidResponse]): Task[Option[BidResponse]] = ???

}

