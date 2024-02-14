package com.example

import io.circe._, io.circe.generic.semiauto._

case class Request(
    start: Long,
    end: Long,
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

object Review {
  implicit val reviewDecoder: Decoder[Review] =
    deriveDecoder[Review]
}

case class ReviewSummary(
    asin: String,
    overall: Double, //this is NOT an average! I was getting confused before!!
    unixReviewTime: Long //TODO when file partitioning is introduced this can be removed if the file names equal the dates
)
object ReviewSummary {
  implicit val reviewSummaryDecoder: Decoder[ReviewSummary] =
    deriveDecoder[ReviewSummary]
}

case class Result(asin: String, averageRating: Double)

case class Error(message: String)
