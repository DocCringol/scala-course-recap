package ru.example.app

import cats.effect.std.Console
import cats.syntax.applicativeError.*
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import cats.{Applicative, Monad, MonadThrow}
import ru.example.client.SearchClient

import scala.util.matching.Regex

private class ConsoleApp[F[_]: MonadThrow: Console](
  client: SearchClient[F]
) {

  import ConsoleApp.*

  def loop: F[Unit] =
    Monad[F].iterateWhile {
      Console[F].readLine.flatTap {
        case searchRegex(query) => searchCommand(query)
        case exit()             => Applicative[F].unit
        case cmd                => Console[F].println(s"Unknown command: '$cmd'")
      }
    } {
      case exit() => false
      case _      => true
    }.void

  private def searchCommand(query: String): F[Unit] =
    client.search(query).attempt.flatMap {
      case Left(throwable) => Console[F].println(s"Exception was thrown: ${throwable.getMessage}")
      case Right(response) =>
        response match {
          case Left(error)    => Console[F].println(s"Error response was returned: $error")
          case Right(succeed) => Console[F].println(s"Succeed response was returned: $succeed")
        }
    }

}

object ConsoleApp {

  def start[F[_]: MonadThrow: Console](
    client: SearchClient[F]
  ): F[Unit] = new ConsoleApp[F](client).loop

  private val searchRegex: Regex = """^search\s+(\w+)$""".r
  private val exit: Regex        = """^exit\s*$""".r

}
