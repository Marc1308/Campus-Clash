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
      output should include("Ort: A-Gebaeude")
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

      output should include("Ungueltige Eingabe.")
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

      output should include("Ort: A-Gebaeude")
      output should include("=== Status ===")
      output should include("Ungueltige Eingabe.")
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
      output should include("Ungueltige Eingabe.")
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
      output should include("Ungueltige Eingabe.")
      output should include("Spiel beendet.")
    }
  }
  "start the A-Building quest and accept the correct answer" in {
    val output = runGame(
      Seq(
        "1", // A-Gebäude betreten
        "3", // Quest anzeigen
        "16", // Richtige Antwort
        "0" // Spiel beenden
      )
    )

    output should include("AGebaeude-Quest")
    output should include("Wie viel Bundeslaender hat Deutschland ?")
    output should include("Right.")
    output should include("Spiel beendet.")
  }

  "start the A-Building quest and reject a wrong answer" in {
    val output = runGame(
      Seq(
        "1",
        "3",
        "15", // Falsche Antwort
        "0"
      )
    )

    output should include("AGebaeude-Quest")
    output should include("False.")
  }

  "start the library quest and accept the correct answer" in {
    val output = runGame(
      Seq(
        "2", // Library betreten
        "3", // Quest anzeigen
        "Berlin", // Richtige Antwort
        "0"
      )
    )

    output should include("Bibliotheck-Quest")
    output should include("Wie heisst die Hauptsstadt von Deutschland ?")
    output should include("Right.")
  }

  "start the library quest and reject a wrong answer" in {
    val output = runGame(
      Seq(
        "2",
        "3",
        "Paris", // Falsche Antwort
        "0"
      )
    )

    output should include("Bibliotheck-Quest")
    output should include("False.")
  }

  "start the mensa quest and accept the correct answer" in {
    val output = runGame(
      Seq(
        "3", // Mensa betreten
        "3", // Quest anzeigen
        "Sprite", // Richtige Antwort
        "0"
      )
    )

    output should include("Mensa-Quest")
    output should include("suesser Sprudel")
    output should include("Right.")
  }

  "start the mensa quest and reject a wrong answer" in {
    val output = runGame(
      Seq(
        "3",
        "3",
        "Cola",
        "0"
      )
    )

    output should include("Mensa-Quest")
    output should include("False.")
  }
