package extensions

import unmarshal.model.Json._
import unmarshal.model.Json
import unmarshal.error.DecoderError
import unmarshal.decoder.Decoder

import scala.reflect.ClassTag
import cats.implicits.{toBifunctorOps, toTraverseOps}

object Extensions {

  implicit class JsonExtensions(json: Json) {

    def parseObject(knownFields: Set[String]): Either[DecoderError, Map[String, Json]] =
      json match {
        case JsonObject(map: Map[String, Json]) =>
          map.keySet.diff(knownFields).toList match {
            case unknownField :: _ => Left(DecoderError("Unknown field", unknownField))
            case _                 => Right(map)
          }
        case _: Json => Left(DecoderError("Expected JsonObject", ""))
      }

  }

  implicit class MapExtensions(map: Map[String, Json]) {

    def getJson[T <: Json: ClassTag](key: String): Either[DecoderError, T] =
      map.get(key) match {
        case Some(value: T) => Right(value)
        case _ =>
          Left(DecoderError(s"Expected ${implicitly[ClassTag[T]].runtimeClass.getSimpleName}", key))
      }

    def getJsonOption[T <: Json: ClassTag](key: String): Either[DecoderError, Option[T]] =
      map.get(key) match {
        case Some(value: T) => Right(Some(value))
        case Some(JsonNull) => Right(None)
        case _ =>
          Left(DecoderError(s"Expected ${implicitly[ClassTag[T]].runtimeClass.getSimpleName}", key))
      }

  }

  implicit class JsonListExtensions(jsonArray: JsonArray) {

    def traverse[T: Decoder](fieldName: String): Either[DecoderError, List[T]] =
      jsonArray.value.zipWithIndex
        .traverse { case (json: Json, index: Int) =>
          Decoder[T]
            .fromJson(json)
            .leftMap(err => DecoderError(err.message, s"$fieldName.$index.${err.field}"))
        }

  }

}
