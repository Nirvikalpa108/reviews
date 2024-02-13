package com.example

import io.circe.parser.decode

import scala.io.Source
import scala.util.{Failure, Success, Using}


//should this be a trait allowing multiple impls?
object FileUtils {
  def parseFile(filePath: String): Either[Error, List[Review]] = {
    Using(Source.fromFile(filePath)) { source => source.mkString } match {
      case Failure(exception) => Left(Error(exception.getMessage))
      case Success(file) => createReviews(file)
    }
  }

  def createReviews(input: String): Either[Error, List[Review]] =
    decode[List[Review]](jsonNewLineDelimitedToString(input)) match {
      case Left(err)      => Left(Error(err.getMessage))
      case Right(reviews) => Right(reviews)
    }

  //TODO just for testing purposes - delete
  def parseReviewSummaryTest(
      input: String
  ): Either[Error, List[ReviewSummary]] = {
    decode[List[ReviewSummary]](jsonNewLineDelimitedToString(input)) match {
      case Left(err)              => Left(Error(err.getMessage))
      case Right(reviewSummaries) => Right(reviewSummaries)
    }
  }

  //don't fully understand this impl, just copy paste job from https://stackoverflow.com/questions/75870869/parse-n-separated-json-with-circe
  private def jsonNewLineDelimitedToString(input: String): String = {
    val rows: List[String] = input.linesIterator.filter(_.trim.nonEmpty).toList
    rows.mkString("[", ",", "]")
  }
}
