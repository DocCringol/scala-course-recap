package ru.example.client

import cats.effect.Temporal
import cats.effect.std.Console
import cats.Functor
import cats.syntax.functor.*
import ru.example.config.SearchClientConfig
import sttp.client4.{Backend, UriContext, basicRequest}

private class GoogleSearchClient[F[_]: Functor](
  config: SearchClientConfig,
  backend: Backend[F]
) extends SearchClient[F] {

  def search(query: String): F[Either[String, String]] =
    basicRequest
      .get(uri"${config.uri}".addParam("q", query)) // https://www.google.com/search?q=${query}
      .send(backend)
      .map(_.body)

}

object GoogleSearchClient {

  // Example of usage
  def apply[F[_]: Console: Temporal](
    config: SearchClientConfig,
    backend: Backend[F]
  ): SearchClient[F] =
    new RetrySearchClient[F](
      new LoggingSearchClient[F](
        new TimeMeasureSearchClient[F](
          new TimoutSearchClient[F](
            new GoogleSearchClient(config, backend),
            config.timeout
          )
        )
      ),
      config.retry
    )

}
