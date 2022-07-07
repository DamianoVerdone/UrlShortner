package com.enelx.shortner.service

import cats.effect.IO
import com.enelx.shortner.model.{LongUrl, ShortUrl}
import com.enelx.shortner.repository.UrlRepository
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.headers.{Host, Location}
import org.http4s.{HttpRoutes, Response, Uri}

// eventually configurable with the domain of the url shortner service ex. https://shortIt.com
final class ShortnerUrlService(domain: Uri) extends Http4sDsl[IO] {


  val routes = HttpRoutes.of[IO] {

    case GET -> Root / externalId =>
      for {
        getResult <- UrlRepository.getLongURL(GenerateShortUrlId.getShortUrlInternalId(externalId))
        response <- urlResult(getResult)
      } yield response

    case req@POST -> Root / "shorten" =>
      for {
        longUrl <- req.decodeJson[LongUrl]
        internalUrlId <- UrlRepository.storeShortUrl(longUrl.destination.toString)
        externalId = GenerateShortUrlId.getShortUrlsExternalId(internalUrlId)
        response <- Created(ShortUrl((domain / externalId).toString(), longUrl.destination.toString))
      } yield response


  }

  private def urlResult(result: Option[String]): IO[Response[IO]] = {
    result match {
      case None => NotFound()
      case Some(longUrl) => MovedPermanently(Location(Uri.unsafeFromString(longUrl)))
    }
  }
}
