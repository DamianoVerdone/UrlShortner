package com.enelx.shortner.servicee

import com.enelx.shortner.service.GenerateShortUrlId
import org.scalatest.funsuite.AnyFunSuite

class GenerateShortUrlIdTest extends AnyFunSuite {


  test("generate external id") {
    assert(GenerateShortUrlId.getShortUrlsExternalId(1) === "baaaab")
    assert(GenerateShortUrlId.getShortUrlsExternalId(0) === "aaaaab")
    assert(GenerateShortUrlId.getShortUrlsExternalId(539505402) === "gbrh7j")
    assert(GenerateShortUrlId.getShortUrlsExternalId(539528750) === "0b9h7j")
  }

  test("generate internal id") {
    assert(GenerateShortUrlId.getShortUrlInternalId("baaaab") === 1)
    assert(GenerateShortUrlId.getShortUrlInternalId("aaaaab") === 0)
    assert(GenerateShortUrlId.getShortUrlInternalId("gbrh7j") === 539505402)
    assert(GenerateShortUrlId.getShortUrlInternalId("0b9h7j") === 539528750)
  }

}
