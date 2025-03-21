package com.example
import cats.Show
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

  "POST /amazon/best-rated should return 200 OK" in {
    val jsonBody: String =
      """
          {
            "start": "0",
            "end": "1708018606",
            "limit": "5",
            "minNumberReviews": "0"
          }
        """
    val request = Request[IO](method = Method.POST, uri = uri"/amazon/best-rated").withEntity(jsonBody)
    val fileService = InMemoryFileService.impl()
    val reviewService = InMemoryReviewService.impl()
    val filePath = "src/test/resources/fullData.txt"
    val expectedBodyResult: Option[List[Result]] = Some(List(Result("B000JQ0JNS", 4.5), Result("B000NI7RW8", 3.666666666666666666666666666666667), Result("B0002F40AY", 3.333333333333333333333333333333333), Result("B00000AQ4N", 3.0), Result("B000654P8C", 2.5)))
    val respIO: IO[Response[IO]] = Routes.reviewServiceRoutes[IO](fileService, reviewService, filePath).orNotFound.run(request)
    check(respIO, Status.Ok, expectedBody = expectedBodyResult) shouldBe true
  }
  // Return true if match succeeds; otherwise false
  def check[A](actual: IO[Response[IO]],
               expectedStatus: Status,
               expectedBody: Option[A])(
                implicit ev: EntityDecoder[IO, A],
                show: Show[A] = Show.fromToString[A] // for nicer diff messages
              ): Unit = {
    val actualResp: Response[IO] = actual.unsafeRunSync()

    withClue("Status code did not match:\n") {
      assertResult(expectedStatus)(actualResp.status)
    }

    expectedBody match {
      case None =>
        val bodyBytes = actualResp.body.compile.toVector.unsafeRunSync()
        withClue(s"Expected empty body, but got: ${new String(bodyBytes.toArray)}\n") {
          assert(bodyBytes.isEmpty)
        }

      case Some(expected) =>
        val actualBody = actualResp.as[A].unsafeRunSync()
        withClue("Body content did not match:\n") {
          assertResult(expected)(actualBody)
        }
    }
  }
}
