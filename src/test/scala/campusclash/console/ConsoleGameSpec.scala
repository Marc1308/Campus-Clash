package campusclash.console

import campusclash.controller.GameController
import campusclash.data.WorldDataLoader
import campusclash.model.{GameState, Player}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, PrintStream}
import java.nio.charset.StandardCharsets

class ConsoleGameSpec extends AnyWordSpec with Matchers:

  private def runGame(inputs: Seq[String]): String =
    val inputStream = new ByteArrayInputStream(
      (inputs.mkString("\n") + "\n").getBytes(StandardCharsets.UTF_8)
    )
    val outputStream = new ByteArrayOutputStream()
    val printStream = new PrintStream(outputStream)

    Console.withIn(inputStream) {
      Console.withOut(printStream) {
        val state = GameState(
          Player("Student", 100, 100, 1),
          None,
          None,
          Set.empty
        )
        val controller = new GameController(state, WorldDataLoader.load())

        new ConsoleGame(controller).start()
      }
    }

    outputStream.toString(StandardCharsets.UTF_8)

  "ConsoleGame" should {
    "show buildings, rooms, and quests loaded from JSON" in {
      val output = runGame(Seq("1", "2", "b", "2", "2", "0"))

      output should include("1 - A Building")
      output should include("2 - Library")
      output should include("You enter the A Building.")
      output should include("Room: Room A101")
      output should include("Federal States: Reach Room A101.")
      output should include("Condition: EnterRoom(a101)")
      output should include("Room: Study Room")
      output should include("Study Time: Reach the study room.")
      output should include("Game ended.")
    }

    "show status and reject invalid campus and building input" in {
      val output = runGame(
        Seq("s", "text", "99", "1", "s", "text", "99", "0")
      )

      output should include("=== Status ===")
      output should include("Name:  Student")
      output should include("HP:    100/100")
      output should include("Level: 1")
      output.sliding("Invalid input.".length)
        .count(_ == "Invalid input.") shouldBe 4
    }

    "allow leaving a building and ending the game on campus" in {
      val output = runGame(Seq("1", "b", "0"))

      output.sliding("Location: Campus".length)
        .count(_ == "Location: Campus") shouldBe 2
      output should include("Game ended.")
    }
  }
