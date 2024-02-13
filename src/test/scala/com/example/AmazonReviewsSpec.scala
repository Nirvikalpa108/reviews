package com.example

import com.example.AmazonReviews.summariseReviews
import org.scalatest.EitherValues
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

class AmazonReviewsSpec extends AnyFreeSpec with Matchers with EitherValues {

  "transforms reviews into review summaries" in {
    val review = Review(
      "B000Q75VCO",
      List(16, 40),
      2.0,
      "Words are in my not-so-humble opinion, the most inexhaustible form of magic we have, capable both of inflicting injury and remedying it.",
      "B07844AAA04E4",
      Some("Gaylord Bashirian"),
      "Ut deserunt adipisci aut.",
      1475261866L
    )
    val input = List(review, review.copy(overall = 3.0), review.copy(overall = 1.0))
    val result = summariseReviews(input)

    result.value.size shouldEqual 3
  }

  "returns reviews within the given time range requested" ignore {
//    val contents: List[String] =
//      Source.fromResource("summarisedData.txt").getLines.toList
//    val reviews: List[ReviewSummary] = parseTest(contents)
//    val startDate = "1661939081" //Wed Aug 31 2022 09:44:41 GMT+0000
//    val endDate = "1662284681" //Sun Sep 04 2022 09:44:41 GMT+0000
//    val request = Request(startDate, endDate, limit = 5, minNumberReviews = 1)
//    val result = searchReviews(request, reviews)
//    result.size shouldEqual 9
  }
  "returns all reviews when the time range requested is from the epoch to today" ignore {}
  "returns no reviews when the time range requested is today" ignore {}
  "returns no reviews when the time requested is in the future" ignore {}

  "returns reviews with the correct number of minimum reviews" ignore {}

  "returns no reviews when the limit requested is zero" ignore {}

  "orders the results with the highest average rated review first" ignore {}

  "handles incorrect requests gracefully" ignore {}
}
