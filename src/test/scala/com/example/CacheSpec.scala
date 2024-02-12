package com.example

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

class CacheSpec extends AnyFreeSpec with Matchers {
  "Cache" - {
    "takes a file row and turns it into a Review" ignore {}
    //TODO decide what to create the cache on - dates or alphabet?
    "gets the unix timestamp and turns it into a DD-MM-YYY (for indexing - this could be the file names)" ignore {}
    "creates one output file for an input file with all reviews on the same day" ignore {}
    "creates three output files for an input file that has reviews across three different days" ignore {}
    "output file names are the unix dates formatted as DD-MM-YYYY" ignore {}
    "given a request for reviews over three days, create a list of size three of the file names to be read" ignore {}
    "given a request for reviews over three days, ensure the file names to be read have the correct names" ignore {}  }
}

//Cache trait?
// def populateCache(inputFilePath: String): Unit
// model: input row and our nicely formatted row
// util defs: unix timestamp work out what the day is, create new file and put the row in it, append row to existing file
