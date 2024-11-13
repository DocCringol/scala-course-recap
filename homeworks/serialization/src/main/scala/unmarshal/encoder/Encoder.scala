package unmarshal.encoder

import unmarshal.model.Json
import unmarshal.model.Json._

import shapeless._
import shapeless.labelled.FieldType

trait Encoder[A] {
  def toJson(value: A): Json
}

object Encoder {

  def apply[A](implicit
    encoder: Encoder[A]
  ): Encoder[A] = encoder

  def autoDerive[A](implicit
    encoder: Encoder[A]
  ): Encoder[A] = encoder

  implicit def stringEncoder: Encoder[String] =
    str => JsonString(str)

  implicit def longEncoder: Encoder[Long] =
    long => JsonNum(long)

  implicit def doubleEncoder: Encoder[Double] =
    double => JsonDouble(double)

  implicit def booleanEncoder: Encoder[Boolean] =
    bool => JsonBool(bool)

  implicit def listEncoder[A](implicit
    enc: Encoder[A]
  ): Encoder[List[A]] =
    list => JsonArray(list.map(enc.toJson))

  implicit def objectEncoder: Encoder[Map[String, Json]] =
    map => JsonObject(map)

  implicit def optionEncoder[A](implicit
    enc: Encoder[A]
  ): Encoder[Option[A]] = {
    case None        => JsonNull
    case Some(value) => enc.toJson(value)
  }

  implicit def hnilEncoder: Encoder[HNil] =
    _ => JsonObject(Map.empty)

  implicit def hlistObjectEncoder[K <: Symbol, H, T <: HList](implicit
    witness: Witness.Aux[K],
    hEncoder: Lazy[Encoder[H]],
    tEncoder: Encoder[T]
  ): Encoder[FieldType[K, H] :: T] = {
    val fieldName: String = witness.value.name
    hlist =>
      val head = hEncoder.value.toJson(hlist.head)
      val tail = tEncoder.toJson(hlist.tail)
      tail match {
        case JsonObject(map) => JsonObject(map + (fieldName -> head))
        case _: Json         => JsonObject(Map(fieldName -> head)) // TODO resolve somehow
        // пытался сделать отдельно ObjectEncoder и createObjectEncoder соответственно для tail
        // но это почему-то ломало деривацию
      }
  }

  implicit def genericEncoder[A, Repr](implicit
    gen: LabelledGeneric.Aux[A, Repr],
    enc: Lazy[Encoder[Repr]]
  ): Encoder[A] =
    value => enc.value.toJson(gen.to(value))

}
