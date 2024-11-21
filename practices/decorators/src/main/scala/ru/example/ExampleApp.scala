package ru.example

import cats.effect.kernel.Async
import cats.effect.std.Console
import cats.effect.{ExitCode, IO, IOApp, Resource}
import ru.example.app.ConsoleApp
import ru.example.client.GoogleSearchClient
import ru.example.config.ApplicationConfig
import sttp.client4.httpclient.cats.HttpClientCatsBackend

object ExampleApp extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    application[IO].attempt
      .flatMap {
        case Left(th) => IO.println(s"Exit with error:${th.getMessage}").as(ExitCode.Error)
        case Right(_) => IO.println("Exit 'Ok'").as(ExitCode.Error)
      }
      .onCancel(IO.println("Exit 'Cancel'"))

  private def application[F[_]: Async: Console]: F[Unit] = (
    for {
      _ <- Resource.make(Console[F].println("Starting application"))(_ =>
        Console[F].println("Application closed")
      )
      config       <- Resource.eval(ApplicationConfig.usafeLoad())
      client       <- HttpClientCatsBackend.resource[F]()
      searchClient <- Resource.pure(GoogleSearchClient(config.searchClient, client))
      _ <- Resource.make(Console[F].println("Application started"))(_ =>
        Console[F].println("Start closing application")
      )
    } yield searchClient
  ).use(ConsoleApp.start(_))

}
