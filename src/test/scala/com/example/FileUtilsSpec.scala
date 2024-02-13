package com.example

import org.scalatest.EitherValues
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import com.example.FileUtils.{
  parseFile,
  createReviews
}

class FileUtilsSpec extends AnyFreeSpec with Matchers with EitherValues {
  "parses raw row and generates Review" in {
    val input =
      """
        |{"asin":"B000Q75VCO","helpful":[16,40],"overall":2.0,"reviewText":"Words are in my not-so-humble opinion, the most inexhaustible form of magic we have, capable both of inflicting injury and remedying it.","reviewerID":"B07844AAA04E4","reviewerName":"Gaylord Bashirian","summary":"Ut deserunt adipisci aut.","unixReviewTime":1475261866}
        |""".stripMargin
    val result = createReviews(input)
    val expected = Right(
      List(
        Review(
          "B000Q75VCO",
          List(16, 40),
          2.0,
          "Words are in my not-so-humble opinion, the most inexhaustible form of magic we have, capable both of inflicting injury and remedying it.",
          "B07844AAA04E4",
          Some("Gaylord Bashirian"),
          "Ut deserunt adipisci aut.",
          1475261866L
        )
      )
    )
    result shouldEqual expected
  }

  "parses multiple raw rows and generates Reviews" in {
    val input =
      """
        |{"asin":"B000Q75VCO","helpful":[16,40],"overall":2.0,"reviewText":"Words are in my not-so-humble opinion, the most inexhaustible form of magic we have, capable both of inflicting injury and remedying it.","reviewerID":"B07844AAA04E4","reviewerName":"Gaylord Bashirian","summary":"Ut deserunt adipisci aut.","unixReviewTime":1475261866}
        |{"asin":"B000NI7RW8","helpful":[32,52],"overall":3.0,"reviewText":"Just because you have the emotional range of a teaspoon doesn’t mean we all have.","reviewerID":"4E82CF3A24D34","reviewerName":"Emilee Heidenreich","summary":"Debitis at facere minus animi quos sed.","unixReviewTime":1455120950}
        |{"asin":"B00000AQ4N","helpful":[35,57],"overall":2.0,"reviewText":"Happiness can be found even in the darkest of times if only one remembers to turn on the light.","reviewerID":"7D04AF18AA084","reviewerName":"Shon Balistreri","summary":"Repellat laborum ab necessitatibus id ut minus repellendus.","unixReviewTime":1571581258}
        |""".stripMargin
    val result = createReviews(input)
    result.value.map(_.asin).distinct.size shouldBe 3
  }

  "reads file and parses into Reviews" in {
    val testFile = "src/test/resources/fullData.txt"
    val result = parseFile(testFile)
    result.value.size shouldBe 15
    result.value.head.asin shouldBe "B000Q75VCO"
  }
}
