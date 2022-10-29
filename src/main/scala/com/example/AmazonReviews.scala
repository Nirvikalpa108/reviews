package com.example

import cats.effect.IO

object AmazonReviews {

  def test(): IO[String] = IO.delay("Hello World!")

  def processReviews(reviews: List[Review]): List[ReviewSummary] = ???
  def searchReviews(request: Request, reviews: List[ReviewSummary]): List[Result] = ???

}
