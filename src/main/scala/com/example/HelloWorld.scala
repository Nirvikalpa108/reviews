package com.example

import cats.effect.IO

object HelloWorld {

  def say(): IO[String] = IO.delay("Hello Cats!")

  //req, data and return answer
  case class Request(start: String, end: String, limit: Int, minNumberReviews: Int)
  case class Review(asin: String, helpful: List[Int], overall: Double, reviewText: String, reviewerID: String, reviewerName: Option[String], summary: String, unixReviewTime: Long)
  case class Result(asin: String, averageRating: Double)

  def searchReviews(request: Request, reviews: List[Review]): List[Result] = ???

}
