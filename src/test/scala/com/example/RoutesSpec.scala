package com.example
import cats.effect.testing.scalatest.AsyncIOSpec
import org.scalatest.freespec.AsyncFreeSpec
import org.scalatest.matchers.should.Matchers
import cats.syntax.all._
import io.circe._
import io.circe.syntax._
import io.circe.generic.semiauto._
import cats.effect._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.implicits._

//https://http4s.org/v1/docs/testing.html
class RoutesSpec extends AsyncFreeSpec with Matchers with AsyncIOSpec {
  //TODO remove this route and test eventually
  "GET /test should return 200 OK with Hello World!" in {
    val request = Request[IO](method = Method.GET, uri = uri"/test")
    val fileService = InMemoryFileService.impl()
    val reviewService = InMemoryReviewService.impl()
    val filePath = ""
    val respIO: IO[Response[IO]] = Routes.reviewServiceRoutes[IO](fileService, reviewService, filePath).orNotFound.run(request)
    check(respIO, Status.Ok, Some("Hello World!")) shouldBe true
  }

  // Return true if match succeeds; otherwise false
  def check[A](actual: IO[Response[IO]],
               expectedStatus: Status,
               expectedBody: Option[A])(
                implicit ev: EntityDecoder[IO, A]
              ): Boolean = {
    val actualResp: Response[IO] = actual.unsafeRunSync()
    val statusCheck: Boolean = actualResp.status == expectedStatus
    val bodyCheck: Boolean = expectedBody.fold[Boolean](
      // Verify Response's body is empty.
      actualResp.body.compile.toVector.unsafeRunSync().isEmpty)(
      expected => actualResp.as[A].unsafeRunSync() == expected
    )
    statusCheck && bodyCheck
  }
}
