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
      /*
      curl --header "Content-Type: application/json" \
        --request POST \
        --data '{"start":"0","end":"1708018606","limit":"5","minNumberReviews":"0"}' \
        http://localhost:8080/amazon/best-rated | jq .
       */
      case req @ POST -> Root / "amazon" / "best-rated" =>
        val maybeResult = for {
          req <- EitherT.right(req.as[Request]) //turn request into Request Type
          //TODO right now we're parsing the file each time this request is run. In the next iteration we should be reading our partitioned files.
          reviews <- EitherT(F.parse(file))
          reviewSummaries <- EitherT(F.transform(reviews)) //transform Reviews into ReviewSummaries
          result <- EitherT(R.getReviews(req, reviewSummaries)) //get Result
        } yield result
        maybeResult.value.flatMap {
          case Left(err)     => InternalServerError(err)
          case Right(result) => Ok(result)
        }

    }
  }
}
