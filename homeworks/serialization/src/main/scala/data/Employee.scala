package data

import unmarshal.decoder.Decoder
import unmarshal.encoder.Encoder

import unmarshal.model.Json._
import unmarshal.model.Json

import extensions.Extensions._

case class Employee(
  name: String,
  age: Long,
  id: Long,
  bossId: Option[Long]
)

object Employee {

  private val knownFields = Set("name", "age", "id", "bossId")

  implicit def employeeEncoder: Encoder[Employee] = (employee: Employee) =>
    JsonObject(
      Map(
        "name"   -> JsonString(employee.name),
        "age"    -> JsonNum(employee.age),
        "id"     -> JsonNum(employee.id),
        "bossId" -> employee.bossId.fold[Json](JsonNull)(JsonNum)
      )
    )

  implicit def employeeDecoder: Decoder[Employee] = (json: Json) =>
    for {
      map    <- json.parseObject(knownFields)
      name   <- map.getJson[JsonString]("name")
      age    <- map.getJson[JsonNum]("age")
      id     <- map.getJson[JsonNum]("id")
      bossId <- map.getJsonOption[JsonNum]("bossId")
    } yield Employee(name.value, age.value, id.value, bossId.map(_.value))

}
