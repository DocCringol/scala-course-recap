package ru.example.client

import cats.{Applicative, MonadThrow}
import cats.syntax.all.*
import cats.effect.std.Console

// Decorator for logging
class LoggingSearchClient[F[_]: Console: MonadThrow] (underlying: SearchClient[F]) extends SearchClient[F] {

  override def search(query: String): F[Either[String, String]] = {
    for {
      _ <- Console[F].println(s"Query $query send")
      result <- underlying.search(query).attempt
      _ <- result match {
        case Left(error) => Console[F].println(s"Query \"$query\" finished with error: $error")
        case Right(Right(answer)) => Console[F].println(s"Query \"$query\" finished successfully with: $answer")
        case Right(Left(error)) => Console[F].println(s"Query \"$query\" finished with error: $error")
      }
      newResult <- Applicative[F].pure(result).rethrow
    } yield newResult
  }

}
