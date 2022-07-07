package com.enelx.shortner

import cats.effect.{ExitCode, IO, IOApp}
import com.enelx.shortner.service.ShortnerUrlService
import org.http4s.Uri
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT


object ServerApp extends IOApp {
  val serverPort = 8080
  println("Server start: http://localhost:8080")
  val urlService = new ShortnerUrlService(Uri.unsafeFromString("http://localhost:8080"))
  val app = (
    urlService.routes
    ).orNotFound

  val server = BlazeServerBuilder[IO]
    .bindHttp(8080)
    .withHttpApp(app)


  def run(args: List[String]): IO[ExitCode] =
    server
      .serve
      .compile.drain
      .as(ExitCode.Success)
}
