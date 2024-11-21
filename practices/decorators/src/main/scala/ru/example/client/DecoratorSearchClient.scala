package ru.example.client

// Dummy
private class DecoratorSearchClient[F[_]] (underlying: SearchClient[F]) extends SearchClient[F] {

  override def search(query: String): F[Either[String, String]] = underlying.search(query)

}
