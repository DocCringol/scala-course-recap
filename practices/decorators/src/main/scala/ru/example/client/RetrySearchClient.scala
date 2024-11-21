package ru.example.client

import cats.Monad
import cats.implicits.toFunctorOps

// Decorator that retries if answer is error-ish <retry> times
class RetrySearchClient[F[_]: Monad](underlying: SearchClient[F], retry: Int)
    extends SearchClient[F] {

  override def search(query: String): F[Either[String, String]] =
    Monad[F].tailRecM[Int, Either[String, String]](0) { cur =>
      underlying.search(query).map {
        case Left(error) if (cur >= retry) => Right(Left(error))
        case Left(_) => Left(cur + 1)
        case r @ Right(_) => Right(r)
      }
    }

}
