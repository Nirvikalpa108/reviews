package com.example

import cats.effect.IO

sealed trait ReviewService[F[_]] {
  def getReviews(
      request: Request,
      reviews: List[ReviewSummary]
  ): F[Either[Error, List[Result]]]
}

object InMemoryReviewService {
  def impl(): ReviewService[IO] = new ReviewService[IO] {
    override def getReviews(
        request: Request,
        reviews: List[ReviewSummary]
    ): IO[Either[Error, List[Result]]] = {
      val withinTimeRange = reviews.filter(review =>
        isWithinTimeRange(request.start, request.end, review)
      )
      val withMinReviews =
        productsWithMinReviews(request.minNumberReviews, withinTimeRange)

      val result = computeReviewAverage(withMinReviews)
        // the minus sign reverses the sort, so that the highest average rating is first
        .sortBy(-_.averageRating)
        .take(request.limit)
      IO(Right(result))
    }
  }

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
