package complex

// Below given by creator (forbidden to change)
final case class ComplexNumber(real: Double, imaginary: Double) {
  def *(other: ComplexNumber) =
    ComplexNumber(
      (real * other.real) - (imaginary * other.imaginary),
      (real * other.imaginary) + (imaginary * other.real)
    )
  def +(other: ComplexNumber) =
    ComplexNumber(real + other.real, imaginary + other.imaginary)
  def ~=(o: ComplexNumber) =
    (real - o.real).abs < 1e-6 && (imaginary - o.imaginary).abs < 1e-6
}

object ComplexNumber {
  // Above given by creator (forbidden to change)

}

object ComplexNumberExtensions {
  import scala.math.{sqrt, atan2, pow, cos, sin}

  final case class PolarComplexNumber(modulus: Double, argument: Double) {
    def toGeneralForm = ComplexNumber(
      modulus * cos(argument),
      modulus * sin(argument)
    )
  }

  given Conversion[ComplexNumber, PolarComplexNumber] = _.toPolarForm
  given Conversion[PolarComplexNumber, ComplexNumber] = _.toGeneralForm
  given [T: Numeric]: Conversion[T, ComplexNumber] = _.toComplex

  extension (complex: ComplexNumber)
    private def minus(otherComplex: ComplexNumber): ComplexNumber =
      ComplexNumber(complex.real - otherComplex.real, complex.imaginary - otherComplex.imaginary)

    def -(otherComplex: ComplexNumber): ComplexNumber = complex.minus(otherComplex)

    private def divide(otherComplex: ComplexNumber): ComplexNumber = {
      val denominator = pow(otherComplex.real, 2) + pow(otherComplex.imaginary, 2)
      if denominator == 0 then throw new ArithmeticException("Division by zero")

      ComplexNumber(
        (complex.real * otherComplex.real + complex.imaginary * otherComplex.imaginary) / denominator,
        (complex.imaginary * otherComplex.real - complex.real * otherComplex.imaginary) / denominator
      )
    }

    def /(otherComplex: ComplexNumber): ComplexNumber = complex.divide(otherComplex)

    def toPolarForm = PolarComplexNumber(
      sqrt(pow(complex.real, 2) + pow(complex.imaginary, 2)),
      atan2(complex.imaginary, complex.real)
    )

  extension [T: Numeric](number: T)
    def apply: Numeric[T] = summon[Numeric[T]]

    def +(complex: ComplexNumber): ComplexNumber = number.toComplex + complex
    def -(complex: ComplexNumber): ComplexNumber = number.minus(complex)
    def *(complex: ComplexNumber): ComplexNumber = number.toComplex * complex
    def /(complex: ComplexNumber): ComplexNumber = number.divide(complex)
    def ~=(complex: ComplexNumber): Boolean = number.toComplex ~= complex

    def toComplex = ComplexNumber(Numeric[T].toDouble(number), 0.0)

    def i: ComplexNumber = ComplexNumber(0.0, Numeric[T].toDouble(number))

  extension [T: Numeric](complex: ComplexNumber)
    def +(number: T): ComplexNumber = number + complex
    def -(number: T): ComplexNumber = number - complex
    def *(number: T): ComplexNumber = number * complex
    def /(number: T): ComplexNumber = number / complex
    def ~=(number: T): Boolean = number ~= complex
}

// Example of usage
@main def main(): Unit = {
  import ComplexNumberExtensions._
  val a = 2 + 3.i
  val b = 3 + 2.i
  println(a * 2.i + 4 / b - 2387 + 3.i)
}
