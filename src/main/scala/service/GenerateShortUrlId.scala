package com.enelx.shortner.service


object GenerateShortUrlId {

  private val SMALLEST_BASE36_VALUE_WITH_6_DIGITS: Int = 60466176
  private val dictionary = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray

  /**
   * Covert an number in a 6 digit base 36 representation.
   *
   * @param lastCounter
   * @return 6 digit representation
   */
  def getShortUrlsExternalId(lastCounter: Int): String = {
    var shortUrlId = SMALLEST_BASE36_VALUE_WITH_6_DIGITS + lastCounter
    val shortUrl = new StringBuilder
    while (shortUrlId > 0) {
      val index = (shortUrlId % dictionary.length)
      shortUrl.append(dictionary(index))
      shortUrlId = shortUrlId / dictionary.length
    }
    shortUrl.toString
  }

  //gbrh7j
  def getShortUrlInternalId(externalId: String): Int =
    externalId.toCharArray.map(
      c => if (c.isDigit) {
        //ascii code 0 + position of 0 in the dictionary
        c.toInt - 48 + 26
      } else
        c.toInt - 97 //ascii code a
    ).foldRight(0) { (elem, value) => (dictionary.length * value) + elem } - SMALLEST_BASE36_VALUE_WITH_6_DIGITS


}
