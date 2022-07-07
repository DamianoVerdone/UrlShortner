import cats.effect.IO
import cats.effect.unsafe.implicits.global
import com.enelx.shortner.ServerApp
import com.enelx.shortner.model.ShortUrl
import io.circe.Json
import io.circe.literal.JsonStringContext
import org.http4s.{Method, Request, Status, Uri}
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.circe.CirceEntityCodec.circeEntityDecoder
import org.http4s.circe.{jsonDecoder, jsonEncoder}
import org.http4s.headers.Location
import org.http4s.headers.Location._
import org.scalatest.BeforeAndAfterAll
import org.scalatest.concurrent.Eventually
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.time.{Millis, Seconds, Span}
import org.typelevel.ci.CIString

class LongUrlServerSpec extends AnyFunSuite with Matchers with BeforeAndAfterAll with Eventually {


  private val client = BlazeClientBuilder[IO].resource


  private val urlStart = s"http://localhost:8080"

  implicit override val patienceConfig: PatienceConfig = PatienceConfig(timeout = scaled(Span(5, Seconds)), interval = scaled(Span(100, Millis)))

  override def beforeAll(): Unit = {
    ServerApp.run(Nil).unsafeRunAsync(_ => ())
    eventually {
      client.use(_.statusFromUri(Uri.unsafeFromString(s"$urlStart"))).unsafeRunSync() shouldBe Status.NotFound
    }
    ()
  }

  test("Invalid Url is not processed") {
    val request = Request[IO](method = Method.POST, uri = Uri.unsafeFromString(s"$urlStart/shorten")).withEntity(createJsonRequest("invalid url"))
    client.use(x => x.status(request)).unsafeRunSync() shouldBe Status.UnprocessableEntity
  }


  test("Inserting 2 o more time same url result in the same short one") {
    val first = createShortUrl("http://www.google.com/test")
    createShortUrl("http://www.google.com/test1")
    val second = createShortUrl("http://www.google.com/test")

    assert(first.shortUrl === second.shortUrl)

  }

  test("a Get request for a missing short url result in a 404") {
    val first = createShortUrl("http://www.google.com/test")
    val request = Request[IO](method = Method.GET, uri = Uri.unsafeFromString(s"$urlStart/missing"))
    client.use(x => x.status(request)).unsafeRunSync() shouldBe Status.NotFound
  }

  test("a Get request for a existing short url result in a 301") {
    val first = createShortUrl("http://www.google.com/test")
    val request = Request[IO](method = Method.GET, uri = Uri.unsafeFromString(first.shortUrl))
    val response = client.flatMap(x => x.run(request))
    response.use(r => IO.pure(r.status)).unsafeRunSync() shouldBe Status.MovedPermanently
    response.use(r => IO.pure(r.headers.get[Location].get.uri.toString())).unsafeRunSync() shouldBe "http://www.google.com/test"
  }

  def createShortUrl(url: String): ShortUrl = {
    val request = Request[IO](method = Method.POST, uri = Uri.unsafeFromString(s"$urlStart/shorten")).withEntity(createJsonRequest(url))
    client.use(x => x.expect[ShortUrl](request)).unsafeRunSync()
  }

  def createJsonRequest(url: String) =
    json"""{
          "destination": ${url}
        }"""

}
