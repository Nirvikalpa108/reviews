package com.example

import cats.effect.IO
import io.circe.parser.decode

import scala.io.Source
import scala.util.{Failure, Success, Using}

/*
A FileService is responsible for parsing a given file and turning it into ReviewSummaries.
There is currently an in-memory implementation of this, but later I will build a streaming impl using fs2.
 */
trait FileService[F[_]] {
  //TODO implement onStart to perform partitioning
  //def onStart(): F[Unit]
  def parse(filePath: String): F[Either[Error, List[Review]]]
  def transform(reviews: List[Review]): List[ReviewSummary]
}

object InMemoryFileService {
  def impl(): FileService[IO] = new FileService[IO] {
    override def parse(filePath: String): IO[Either[Error, List[Review]]] = {
      IO(Using(Source.fromFile(filePath)) { source => source.mkString } match {
        case Failure(exception) => Left(Error(exception.getMessage))
        case Success(file)      => createReviews(file)
      })
    }

    //TODO not sure about the type signature - is there ever a Left situation?
    override def transform(
        reviews: List[Review]
    ): List[ReviewSummary] =
      reviews.map(r => ReviewSummary(r.asin, r.overall, r.unixReviewTime))

    private def createReviews(input: String): Either[Error, List[Review]] =
      decode[List[Review]](jsonNewLineDelimitedToString(input)) match {
        case Left(err)      => Left(Error(err.getMessage))
        case Right(reviews) => Right(reviews)
      }

    /* takes a newline-delimited JSON string and converts it into a valid JSON array string.
    https://stackoverflow.com/questions/75870869/parse-n-separated-json-with-circe
     */
    private def jsonNewLineDelimitedToString(input: String): String = {
      val rows: List[String] =
        input.linesIterator.filter(_.trim.nonEmpty).toList
      rows.mkString("[", ",", "]")
    }
  }
}
