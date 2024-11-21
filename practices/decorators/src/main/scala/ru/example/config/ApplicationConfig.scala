package ru.example.config

import cats.effect.Sync
import pureconfig.generic.ProductHint
import pureconfig.generic.semiauto.deriveReader
import pureconfig.module.catseffect.syntax.CatsEffectConfigSource
import pureconfig.{ConfigReader, ConfigSource}

import scala.annotation.unused

case class ApplicationConfig(
  searchClient: SearchClientConfig
)

object ApplicationConfig {

  @unused private implicit def hint[T]: ProductHint[T] = ProductHint[T](allowUnknownKeys = false)

  implicit val configReader: ConfigReader[ApplicationConfig] = {
    @unused implicit val searchClientReader: ConfigReader[SearchClientConfig] =
      deriveReader[SearchClientConfig]

    deriveReader[ApplicationConfig]
  }

  def usafeLoad[F[_]: Sync](
    config: ConfigSource = ConfigSource.default
  ): F[ApplicationConfig] =
    config.at("ru.example").loadF[F, ApplicationConfig]()

}
