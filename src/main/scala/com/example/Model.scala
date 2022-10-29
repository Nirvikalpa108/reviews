package com.example

case class Request(
    start: String,
    end: String,
    limit: Int,
    minNumberReviews: Int
)

case class Review(
    asin: String,
    helpful: List[Int],
    overall: Double,
    reviewText: String,
    reviewerID: String,
    reviewerName: Option[String],
    summary: String,
    unixReviewTime: Long
)

case class ReviewSummary(
    asin: String,
    overall: Double,
    unixReviewTime: Long
)

case class Result(asin: String, averageRating: Double)
