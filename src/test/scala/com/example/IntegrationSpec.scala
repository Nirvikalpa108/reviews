package com.example

import com.example.AmazonReviews.summariseReviews
import com.example.FileUtils.parseFile
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

class IntegrationSpec extends AnyFreeSpec with Matchers {
  "reads file and transforms into Review Summaries" in {
    val testFile = "src/test/resources/fullData.txt"
    for {
      reviews <- parseFile(testFile)
      reviewSummaries <- summariseReviews(reviews)
    } yield {
      reviewSummaries.size shouldBe reviews.size
      //any more effective assertions?
    }
  }
}
