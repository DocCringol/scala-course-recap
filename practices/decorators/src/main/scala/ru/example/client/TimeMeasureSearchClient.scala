package ru.example.client

import cats.Monad
import cats.syntax.all.*
import cats.effect.kernel.Clock
import cats.effect.std.Console

// Decorator that logs running time of request
class TimeMeasureSearchClient [F[_]: Clock: Monad: Console](underlying: SearchClient[F]) extends SearchClient[F] {

  override def search(query: String): F[Either[String, String]] = {
    for {
      (timed, result) <- Clock[F].timed(underlying.search(query))
      _ <- Console[F].println(s"Request duration: ${timed.toMillis}ms")
    } yield result
  }

}