package ru.example.client

trait SearchClient[F[_]] {
  // TODO: use your own model (in query, and response) instead of String
  def search(query: String): F[Either[String, String]]
}
