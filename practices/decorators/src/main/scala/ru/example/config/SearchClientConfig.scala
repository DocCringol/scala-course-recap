package ru.example.config

import scala.concurrent.duration.FiniteDuration

case class SearchClientConfig(
  timeout: FiniteDuration,
  uri: String,
  retry: Int
)
