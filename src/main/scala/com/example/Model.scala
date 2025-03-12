package com.example

import cats.effect.IO
import io.circe._
import io.circe.generic.semiauto._
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe._
import io.circe.generic.auto._

//FYI the start and end values are strings in date.month.year format, so they need converting into Unix timestamps
case class Request(
    start: Long,
    end: Long,
    limit: Int,
    minNumberReviews: Int
)

object Request {
  implicit val requestEntityDecoder: EntityDecoder[IO, Request] =
    jsonOf[IO, Request]
  implicit val requestEntityEncoder: EntityEncoder[IO, Request] =
    jsonEncoderOf[IO, Request]
}

case class Review(
    asin: String,
    helpful: List[Int],
    overall: BigDecimal,
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
    overall: BigDecimal, //this is NOT an average! I was getting confused before!!
    unixReviewTime: Long //TODO when file partitioning is introduced this can be removed if the file names equal the dates
)
object ReviewSummary {
  implicit val reviewSummaryDecoder: Decoder[ReviewSummary] =
    deriveDecoder[ReviewSummary]
}

case class Result(asin: String, averageRating: BigDecimal)

object Result {
  implicit val resultEncoder: Encoder[Result] = deriveEncoder[Result]
  implicit val resultEntityEncoder: EntityEncoder[IO, List[Result]] =
    jsonEncoderOf[IO, List[Result]]

}

case class Error(message: String)

object Error {
  implicit val errorEntityEncoder: EntityEncoder[IO, Error] =
    jsonEncoderOf[IO, Error]
}
