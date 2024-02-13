package com.example

import cats.effect.IO

object AmazonReviews {

  def test(): IO[String] = IO.delay("Hello World!")

  // we don't need all of the input data, just some of it to respond to client requests, so here we perform that transformation
  def summariseReviews(reviews: List[Review]): Either[Error, List[ReviewSummary]] = {
    Right(reviews.map { r =>
      ReviewSummary(r.asin, r.overall, r.unixReviewTime)
    })
  }

  def searchReviews(
      request: Request,
      reviews: List[ReviewSummary]
  ): Either[Error, List[Result]] = {
    // val asinToSummaries = summaries.groupBy(_.asin)
    //    Right(asinToSummaries.map { case (asin, summaries) =>
    //      val ratings = summaries.map(_.overall)
    //      val average = ratings.sum / ratings.length
    //      ReviewSummary(asin, average,)
    //    }.toList)
    ???
  }
}
