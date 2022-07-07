package com.enelx.shortner.model

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class ShortUrl(shortUrl:Option[String], destination:String)

object ShortUrl {

  implicit val shortUrlEncoder: Encoder[ShortUrl] = deriveEncoder[ShortUrl]

  implicit val shortUrlDecoder: Decoder[ShortUrl] = deriveDecoder[ShortUrl]

}
