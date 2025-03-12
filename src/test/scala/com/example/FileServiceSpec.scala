package com.example

import cats.effect.testing.scalatest.AsyncIOSpec
import org.scalatest.EitherValues
import org.scalatest.freespec.AsyncFreeSpec
import org.scalatest.matchers.should.Matchers

class FileServiceSpec extends AsyncFreeSpec with Matchers with EitherValues with AsyncIOSpec {
  "reads file and parses into Reviews" in {
    val testFilePath = "src/test/resources/fullData.txt"
    val result = InMemoryFileService.impl().parse(testFilePath)
    result.asserting(_.value.size shouldBe 15)
    result.asserting(_.value.head.asin shouldBe "B000Q75VCO")
  }
  "returns an empty list when parsing an empty file"  in {
    val emptyFilePath = "src/test/resources/emptyFile.txt"
    val result = InMemoryFileService.impl().parse(emptyFilePath)
    result.asserting(_.value shouldBe empty)
  }
  //TODO would be nice to have helpful error messages inside the Left Value
  "fails gracefully when the file does not exist" in {
    val nonExistentFilePath = "src/test/resources/nonExistent.txt"
    val result = InMemoryFileService.impl().parse(nonExistentFilePath)
    result.asserting(_.isLeft shouldBe true)
  }
  "fails gracefully when the file contains invalid data" in {
    val invalidFilePath = "src/test/resources/invalidData.txt"
    val result = InMemoryFileService.impl().parse(invalidFilePath)
    result.asserting(_.isLeft shouldBe true)
  }
  //TODO create these files before the tests are run and delete afterwards
  //This would mean I could enable the test below 
  "returns an appropriate error when file permissions prevent reading" ignore {
    val restrictedFilePath = "src/test/resources/restricted.txt"
    val result = InMemoryFileService.impl().parse(restrictedFilePath)
    result.asserting(_.isLeft shouldBe true)
  }
}
