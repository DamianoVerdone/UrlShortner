package com.enelx.shortner.repository

import cats.effect.IO

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer, ListBuffer}

object UrlRepository {
  private val maxNumberOfUrls= 900000
  private val idCounter = new AtomicInteger()
  private val longUrlToShortId: ConcurrentHashMap[String, Int] = new ConcurrentHashMap()
  private val shortToLongUrl = new Array[String](maxNumberOfUrls) //to avoid resize an array copy

  def getLongURL(id:Int): IO[Option[String]] = {
    IO.pure(shortToLongUrl.lift(id))
  }


  def storeShortUrl(longUrl: String): IO[Int] =
    IO.pure {
     longUrlToShortId.computeIfAbsent(longUrl, _ => {
       val i = idCounter.getAndIncrement()
       shortToLongUrl.update(i, longUrl)
       i
     })
    }


}
