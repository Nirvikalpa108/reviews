package com.example

import com.example.HelloWorld.{Request, Review, searchReviews}
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

import scala.io.Source

class HelloWorldTest extends AnyFreeSpec with Matchers {

  "returns correct reviews within the given time range requested" in {
    val contents: List[String] = Source.fromResource("test.txt").getLines.toList
    val reviews: List[Review] = ??? //codecs
    val request = Request("", "", 5, 1)
    val result = searchReviews(request, reviews)
    result shouldEqual Nil
  }
  "returns all reviews when the time range requested is from the epoch to today" ignore {}
  "returns no reviews when the time range requested is a day in the future" ignore {}
  "returns reviews with the correct number of minimum reviews" ignore {}
  "returns no results when the limit requested is zero" ignore {}
  "handles incorrect requests gracefully" ignore {}
  "orders the results by highest average rating" ignore {}
}
