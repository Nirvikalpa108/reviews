package com.example

import com.example.AmazonReviews._
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

import scala.io.Source

class AmazonReviewsTest extends AnyFreeSpec with Matchers {

  "returns reviews within the given time range requested" ignore {
    val contents: List[String] =
      Source.fromResource("summarisedData.txt").getLines.toList
    val reviews: List[ReviewSummary] = ??? //codecs
    val request = Request("", "", 5, 1)
    val result = searchReviews(request, reviews)
    result shouldEqual Nil
  }
  "returns all reviews when the time range requested is from the epoch to today" ignore {}
  "returns no reviews when the time range requested is today" ignore {}
  "returns no reviews when the time requested is in the future" ignore {}
  "returns reviews with the correct number of minimum reviews" ignore {}
  "returns no reviews when the limit requested is zero" ignore {}
  "orders the results with the highest average rated review first" ignore {}
  "handles incorrect requests gracefully" ignore {}
}
