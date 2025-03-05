package com.example

import cats.effect.{Async, IO}
import com.comcast.ip4s._
import fs2.io.net.Network
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import org.http4s.server.middleware.Logger

//TODO where is my middleware? Add this.
object Server {
  def run[F[_]: Async: Network](filePath: String): IO[Nothing] = {
    val fileService = InMemoryFileService.impl()
    val reviewService = InMemoryReviewService.impl()

    val httpApp = Routes
      .reviewServiceRoutes[IO](fileService, reviewService, filePath)
      .orNotFound
    val finalHttpApp = Logger.httpApp(true, true)(httpApp)

    for {
      _ <-
        EmberServerBuilder
          .default[IO]
          .withHost(ipv4"0.0.0.0")
          .withPort(port"8080")
          .withHttpApp(finalHttpApp)
          .build
    } yield ()
  }.useForever
}
