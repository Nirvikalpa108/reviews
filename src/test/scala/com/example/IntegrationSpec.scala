package com.example

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import cats.data.EitherT


class IntegrationSpec extends AnyFreeSpec with Matchers {
  "reads file and transforms into Review Summaries" in {
    val fileService = InMemoryFileService.impl()
    val reviewService = InMemoryReviewService.impl()
    val testFile = "src/test/resources/fullData.txt"
    val now = System.currentTimeMillis / 1000
    val request: Request = Request(start = 0, end = now, limit = 5, minNumberReviews = 0)
    for {
      reviews <- EitherT(fileService.parse(testFile))
      reviewSummaries = fileService.transform(reviews)
      result <- EitherT(reviewService.getReviews(request, reviewSummaries))
    } yield {
      result.size shouldBe 15
    }
  }
}
