package com.example

import cats.effect.IO

/* The ReviewService is responsible for taking a user's request and a list of ReviewSummaries and returning a result to the user.*/
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
      //TODO this will change in the next iteration, so that we only open files of a certain date
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

  // checks a given review is somewhere within a time range
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
    //TODO a shorter alternative, but harder to read?
    /*
    val productAsinsToKeep: Map[String, Int] = allReviews.groupMapReduce(_.asin)(_ => 1)(_ + _) // Groups by asin and counts occurrences
    allReviews.filter(r => productAsinsToKeep.getOrElse("", 0) >= minReviews)
     */
    val productAsinsToKeep: List[String] = allReviews
      //groups by the unique asins, which are the unique product identifiers
      .groupBy(r => r.asin)
      // gets the number of reviews for each product
      .map { case (product, reviews) => (product, reviews.size) }
      // filters out products which don't have enough reviews
      .filter { case (_, numberOfReviews) => numberOfReviews >= minReviews }
      // returns the product identifiers themselves
      .keys
      .toList
    allReviews.filter(r => productAsinsToKeep.contains(r.asin))
  }

  def computeReviewAverage(allReviews: List[ReviewSummary]): List[Result] = {
    //groupBy the asin, unique product identifier
    val productToReviews: Map[String, List[ReviewSummary]] = allReviews.groupBy(_.asin)
    //get the average rating and turn therefore turn the ReviewSummaries into the Result Type
    productToReviews.map { case (product, reviewSummaries) =>
      val averageRating: BigDecimal =
        reviewSummaries.map(_.overall).sum / reviewSummaries.size
      Result(product, averageRating)
    }.toList
  }
}
