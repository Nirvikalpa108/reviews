package com.example

import cats.effect.IOApp
import cats.effect.IO

object Main extends IOApp.Simple {
  def run: IO[Unit] = {
    val filePath: String = "src/test/resources/fullData.txt" //TODO get from config
    Server.run[IO](filePath)
  }
}
