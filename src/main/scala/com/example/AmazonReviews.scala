package com.example

import cats.effect.IO

object AmazonReviews {

  def test(): IO[String] = IO.delay("Hello World!")

  def parseFile(rows: List[String]): List[Review] = ???
  def processReviews(reviews: List[Review]): List[ReviewSummary] = ???
  def searchReviews(request: Request, reviews: List[ReviewSummary]): List[Result] = ???

}
