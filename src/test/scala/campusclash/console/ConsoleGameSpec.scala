package campusclash.console

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.nio.charset.StandardCharsets

class ConsoleGameTest extends AnyWordSpec with Matchers:

  private def runGame(inputs: Seq[String]): String =
    val inputText = inputs.mkString("\n") + "\n"

    val inputStream =
      new ByteArrayInputStream(
        inputText.getBytes(StandardCharsets.UTF_8)
      )

    val outputStream = new ByteArrayOutputStream()
    val printStream = new PrintStream(outputStream)

    Console.withIn(inputStream) {
      Console.withOut(printStream) {
        ConsoleGame.start()
      }
    }

    outputStream.toString(StandardCharsets.UTF_8)

  "ConsoleGame" should {

    "allow the player to visit all locations" in {
      val output = runGame(
        Seq(
          "1", // Campus -> BuildingA
          "1", // BuildingA -> Campus
          "2", // Campus -> Library
          "1", // Library -> Campus
          "3", // Campus -> Mensa
          "1", // Mensa -> Campus
          "0"  // Beenden
        )
      )

      output should include("Ort: Campus")
      output should include("Ort: A-Gebäude")
      output should include("Ort: Library")
      output should include("Ort: Mensa")
      output should include("Spiel beendet.")
    }

    "show the player status" in {
      val output = runGame(
        Seq(
          "4",
          "0"
        )
      )

      output should include("=== Status ===")
      output should include("Name:  Student")
      output should include("HP:    100/100")
      output should include("Level: 1")
    }

    "handle invalid input on the campus" in {
      val output = runGame(
        Seq(
          "x",
          "0"
        )
      )

      output should include("Ungültige Eingabe.")
    }

    "handle status and invalid input in building A" in {
      val output = runGame(
        Seq(
          "1",
          "2",
          "x",
          "0"
        )
      )

      output should include("Ort: A-Gebäude")
      output should include("=== Status ===")
      output should include("Ungültige Eingabe.")
      output should include("Spiel beendet.")
    }

    "handle status and invalid input in the library" in {
      val output = runGame(
        Seq(
          "2",
          "2",
          "x",
          "0"
        )
      )

      output should include("Ort: Library")
      output should include("=== Status ===")
      output should include("Ungültige Eingabe.")
      output should include("Spiel beendet.")
    }

    "handle status and invalid input in the mensa" in {
      val output = runGame(
        Seq(
          "3",
          "2",
          "x",
          "0"
        )
      )

      output should include("Ort: Mensa")
      output should include("=== Status ===")
      output should include("Ungültige Eingabe.")
      output should include("Spiel beendet.")
    }
  }