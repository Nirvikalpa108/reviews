package com.example

import cats.effect.IO

object AmazonReviews {

  def test(): IO[String] = IO.delay("Hello World!")

  // we don't need all of the input data, just some of it to respond to client requests, so here we perform that transformation
  def summariseReviews(
      reviews: List[Review]
  ): Either[Error, List[ReviewSummary]] = {
    Right(reviews.map { r =>
      ReviewSummary(r.asin, r.overall, r.unixReviewTime)
    })
  }

  def searchReviews(
      request: Request,
      reviews: List[ReviewSummary]
  ): Either[Error, List[Result]] = {
    val withinTimeRange = reviews.filter(review =>
      isWithinTimeRange(request.start, request.end, review)
    )
    val withMinReviews =
      productsWithMinReviews(request.minNumberReviews, withinTimeRange)
    //TODO is there a Left scenario now? Could there be one in the future?
    Right(
      computeReviewAverage(withMinReviews)
        // the minus sign reverses the sort, so that the highest average rating is first
        .sortBy(-_.averageRating)
        .take(request.limit)
    )
  }

  //TODO make these private and change tests to access them
  def isWithinTimeRange(
      start: Long,
      end: Long,
      review: ReviewSummary
  ): Boolean = {
    review.unixReviewTime >= start && review.unixReviewTime <= end
  }

  def productsWithMinReviews(
      minReviews: Int,
      allReviews: List[ReviewSummary]
  ): List[ReviewSummary] = {
    val productsToKeep = allReviews
      .groupBy(r => r.asin)
      .map { case (product, reviews) => (product, reviews.size) }
      .filter { case (_, numberOfReviews) => numberOfReviews >= minReviews }
      .keys
      .toList
    allReviews.filter(r => productsToKeep.contains(r.asin))
  }

  def computeReviewAverage(allReviews: List[ReviewSummary]): List[Result] = {
    val productToReviews: Map[String, List[ReviewSummary]] =
      allReviews.groupBy(_.asin)
    productToReviews.map { case (product, reviewSummaries) =>
      val averageRating: Double =
        reviewSummaries.map(_.overall).sum / reviewSummaries.size
      Result(product, averageRating)
    }.toList
  }

}
