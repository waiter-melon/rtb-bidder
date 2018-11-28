package com.powerspace.bidding

import cats.data.OptionT
import com.google.openrtb.{BidRequest, BidResponse}
import monix.eval.Task
//
//class RtbBidder[F[_]] extends Bidder[F] {
//
//  override def bidOn(bidRequest: BidRequest): F[Option[BidResponse]] = {
//    val responses = bidRequest.imp.map(imp => scatterGather(bidRequest, imp).value)
//
//    Task.gatherUnordered(responses)
//      .map(_.flatten)
//      .map(maybeResponses => {
//        Some(maybeResponses
//          .reduceOption(???)
//          .getOrElse(???))
//      })
//  }
//
//  private def scatterGather(request: BidRequest, imp: BidRequest.Imp): OptionT[Task, BidResponse] = ???
//
//}
