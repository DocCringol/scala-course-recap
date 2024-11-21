package ru.example.client

import cats.Monad
import cats.effect.Temporal
import cats.effect.implicits.genTemporalOps_

import scala.concurrent.duration.FiniteDuration

// Decorator that stops search after <timeout> time
class TimoutSearchClient[F[_]: Temporal](underlying: SearchClient[F], timeout: FiniteDuration) extends SearchClient[F] {

  override def search(query: String): F[Either[String, String]] =
    underlying.search(query).timeoutTo(timeout, Monad[F].pure(Left("Timeout")))

}