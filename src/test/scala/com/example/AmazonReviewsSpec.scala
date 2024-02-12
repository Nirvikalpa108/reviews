package com.example

import com.example.AmazonReviews._
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

import scala.io.Source

class AmazonReviewsSpec extends AnyFreeSpec with Matchers {

  "returns reviews within the given time range requested" ignore {
    val contents: List[String] =
      Source.fromResource("summarisedData.txt").getLines.toList
    val reviews: List[ReviewSummary] = parseFile(contents)
    val startDate = "1661939081" //Wed Aug 31 2022 09:44:41 GMT+0000
    val endDate = "1662284681" //Sun Sep 04 2022 09:44:41 GMT+0000
    val request = Request(startDate, endDate, limit = 5, minNumberReviews = 1)
    val result = searchReviews(request, reviews)
    result.size shouldEqual 9
  }
  "returns all reviews when the time range requested is from the epoch to today" ignore {}
  "returns no reviews when the time range requested is today" ignore {}
  "returns no reviews when the time requested is in the future" ignore {}
  "returns reviews with the correct number of minimum reviews" ignore {}
  "returns no reviews when the limit requested is zero" ignore {}
  "orders the results with the highest average rated review first" ignore {}
  "handles incorrect requests gracefully" ignore {}

  def parseFile(rows: List[String]): List[ReviewSummary] = ???
  //{"asin":"B00000AQ4N","overall":1.0.","unixReviewTime":1662185805}
  //{"asin":"B00000AQ4N"
  //"overall":1.0."
  //"unixReviewTime":1662185805} -
  // only take certain number of chars from the end of each after splitting?

  //scala> val row: String = """{"asin":"B00000AQ4N","overall":1.0.","unixReviewTime":1662185805}"""
  //val row: String = {"asin":"B00000AQ4N","overall":1.0.","unixReviewTime":1662185805}
  //
  //scala> row.split(",")
  //val res0: Array[String] = Array({"asin":"B00000AQ4N", "overall":1.0.", "unixReviewTime":1662185805})
  //
  //scala> val rowSplit = row.split(",")
  //val rowSplit: Array[String] = Array({"asin":"B00000AQ4N", "overall":1.0.", "unixReviewTime":1662185805})
  //
  //scala> rowSplit.head
  //val res1: String = {"asin":"B00000AQ4N"
  //
  //scala> rowSplit.head.drop(9)
  //val res2: String = B00000AQ4N"
  //
  //scala> rowSplit.head.drop(9).dropRight(1)
  //val res3: String = B00000AQ4N
}
