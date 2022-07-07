package com.enelx.shortner.model

import io.circe.Decoder.Result
import io.circe.{Decoder, DecodingFailure, HCursor}

import java.net.URL
import scala.util.Try

case class LongUrl(destination: URL)

object LongUrl {


  implicit val longUrlDecoder = new Decoder[LongUrl] {
    override def apply(c: HCursor): Result[LongUrl] = {
      for {
        destination <- c.downField("destination").as[String]
        url <- Try(new URL(destination)).toEither.left.map(t =>{
          DecodingFailure.fromThrowable(t, c.history)
        } )

      } yield LongUrl(url)

    }
  }
}

