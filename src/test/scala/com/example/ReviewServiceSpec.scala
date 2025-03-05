package com.example

import cats.effect.testing.scalatest.AsyncIOSpec
import com.example.InMemoryReviewService._
import org.scalatest.EitherValues
import org.scalatest.freespec.AsyncFreeSpec
import org.scalatest.matchers.should.Matchers

class ReviewServiceSpec
    extends AsyncFreeSpec
    with Matchers
    with EitherValues
    with AsyncIOSpec {

  val reviews: List[ReviewSummary] = List(
    ReviewSummary("B1", 2, 1661939080),
    ReviewSummary("B1", 4, 1662284683),
    ReviewSummary("B2", 4, 1661939082),
    ReviewSummary("B2", 1, 1661939082),
    ReviewSummary("B3", 3, 1662284682),
    ReviewSummary("B3", 7.57777777777, 1662284682)
  )
  val today = System.currentTimeMillis / 1000

  "returns reviews within the given time range requested" in {
    val startDate = 1661939081 //Wed Aug 31 2022 09:44:41 GMT+0000
    val endDate = 1662284681 //Sun Sep 04 2022 09:44:41 GMT+0000
    val result = reviews.filter(r => isWithinTimeRange(startDate, endDate, r))
    result.size shouldEqual 2
  }
  "returns all reviews when the time range requested is from the epoch to today" in {
    val startDate = 0
    val result = reviews.filter(r => isWithinTimeRange(startDate, today, r))
    result.size shouldEqual reviews.size
  }
  "returns no reviews when the time range requested is today" in {
    val result = reviews.filter(r => isWithinTimeRange(today, today, r))
    result shouldBe empty
  }
  "returns reviews on a given day when the start date and end date are the same" in {
    val sameDate = reviews.head.unixReviewTime
    val result = reviews.filter(r => isWithinTimeRange(sameDate, sameDate, r))
    result.size shouldEqual 1
  }

  "returns all reviews when the limit requested is zero" in {
    val minReviews = 0
    val result = productsWithMinReviews(minReviews, reviews)
    result.size shouldBe reviews.size
  }
  "returns no reviews when the limit requested is very high" in {
    val minReviews = 1000
    val result = productsWithMinReviews(minReviews, reviews)
    result shouldBe empty
  }
  "returns reviews when there are products that have more than the minimum number of reviews" in {
    val minReviews = 2
    val result = productsWithMinReviews(minReviews, reviews)
    result shouldNot be(empty) //should I make this more specific?
  }

  "when computing the average rating, returns a list of unique products" in {
    val result = computeReviewAverage(reviews)
    result.size shouldEqual reviews.map(_.asin).distinct.size
  }
  "when computing the average rating, does not change a list where every product asin is unique" in {
    val reviews = List(
      ReviewSummary("B1", 2, 1661939080),
      ReviewSummary("B2", 4, 1662284683),
      ReviewSummary("B3", 4, 1661939082)
    )
    val result = computeReviewAverage(reviews)
    result.size shouldEqual reviews.size
  }
  "computes average rating for each product" in {
    val result = computeReviewAverage(reviews)
    result.filter(_.asin == "B1").map(_.averageRating) shouldEqual List(3)
  }
  "computes a math floor average rating" ignore { ??? }
  "handles decimal places as expected when computing the average rating" ignore {
    ???
  }

  "sorts the results with the highest average rated review first" in {
    val request =
      Request(start = 0, end = today, limit = 10, minNumberReviews = 0)
    val result = InMemoryReviewService.impl().getReviews(request, reviews)

    result.asserting(_.value.headOption.map(_.asin) shouldBe Some("B3"))
  }
  //TODO are there any further sorting tests I would like?
  "returns an empty list when the requested limit is zero" in {
    val request =
      Request(start = 0, end = today, limit = 0, minNumberReviews = 0)
    val result = InMemoryReviewService.impl().getReviews(request, reviews)
    result.asserting(_.value shouldBe empty)
  }
  "returns all elements in the list when the request size matches the list length" in {
    val request = Request(
      start = 0,
      end = today,
      limit = reviews.size,
      minNumberReviews = 0
    )
    val result = InMemoryReviewService.impl().getReviews(request, reviews)
    result.asserting(_.value.size shouldBe reviews.map(_.asin).distinct.size)
  }
  //TODO check other size things - what happens when the size is greater than the list length? what if it is less?
  "handles incorrect requests gracefully" ignore { ??? }
}
