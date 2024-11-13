package data

import unmarshal.decoder.Decoder
import unmarshal.encoder.Encoder

import unmarshal.model.Json._
import unmarshal.model.Json

import extensions.Extensions._

case class CompanyEmployee(
  employees: List[Employee]
)

object CompanyEmployee {

  private val knownFields = Set("employees")

  implicit def companyEmployeeEncoder: Encoder[CompanyEmployee] =
    (companyEmployee: CompanyEmployee) =>
      JsonObject(
        Map(
          "employees" -> JsonArray(
            companyEmployee.employees.map((employee: Employee) =>
              implicitly[Encoder[Employee]].toJson(employee)
            )
          )
        )
      )

  implicit def companyEmployeeDecoder: Decoder[CompanyEmployee] = (json: Json) =>
    for {
      map          <- json.parseObject(knownFields)
      jsonArray    <- map.getJson[JsonArray]("employees")
      listEmployee <- jsonArray.traverse[Employee]("employees")
    } yield CompanyEmployee(listEmployee)

}
