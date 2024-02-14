package com.example

import cats.data.EitherT
import cats.effect.IO
import cats.effect.Sync
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

object Routes {
  def reviewServiceRoutes[F[_]: Sync](
      F: FileService[IO],
      R: ReviewService[IO],
      file: String
  ): HttpRoutes[IO] = {
    val dsl = new Http4sDsl[IO] {}
    import dsl._
    HttpRoutes.of[IO] {
      case GET -> Root / "test" => Ok("Hello World!")
      case req @ POST -> Root / "amazon" / "best-rated" =>
        //TODO work out how to parse the req correctly
        val req: Request =
          Request(start = 0, end = 1708018606, limit = 5, minNumberReviews = 0)
        val maybeResult = for {
          //req <- EitherT.right(req.as[Request])
          reviews <- EitherT(F.parse(file))
          reviewSummaries <- EitherT(F.transform(reviews))
          result <- EitherT(R.getReviews(req, reviewSummaries))
        } yield result
        maybeResult.value.flatMap {
          case Left(err)     => InternalServerError(err)
          case Right(result) => Ok(result)
        }

    }
  }
}
