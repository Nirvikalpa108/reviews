package com.example

import cats.effect.testing.scalatest.AsyncIOSpec
import org.scalatest.EitherValues
import org.scalatest.freespec.AsyncFreeSpec
import org.scalatest.matchers.should.Matchers

//TODO is this a good test? How can I improve it?
class FileServiceSpec extends AsyncFreeSpec with Matchers with EitherValues with AsyncIOSpec {
  "reads file and parses into Reviews" in {
    val testFile = "src/test/resources/fullData.txt"
    val result = InMemoryFileService.impl().parse(testFile)
    result.asserting(_.value.size shouldBe 15)
    result.asserting(_.value.head.asin shouldBe "B000Q75VCO")
  }
}
