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

  private def countOccurrences(text: String, fragment: String): Int =
    text.sliding(fragment.length).count(_ == fragment)

  "ConsoleGame" should {

    "show buildings, rooms, and quests loaded from JSON" in {
      val output = runGame(Seq("1", "2", "b", "2", "2", "0"))

      output should include("=== Campus Clash ===")
      output should include("Buildings:")
      output should include("1 - A Building")
      output should include("2 - Library")

      output should include("You enter the A Building.")
      output should include("=== A Building ===")
      output should include("Rooms:")
      output should include("1 - Entrance Hall")
      output should include("2 - Room A101")
      output should include("Room: Room A101")

      output should include("Quests:")
      output should include("Federal States: Reach Room A101.")
      output should include("Condition: EnterRoom(a101)")

      output should include("=== Library ===")
      output should include("Room: Study Room")
      output should include("2 - Study Room")
      output should include("Study Time: Reach the study room.")

      output should include("b - Leave building")
      output should include("Game ended.")
    }

    "show all menu options and input prompts" in {
      val campusOutput = runGame(Seq("0"))

      campusOutput should include("=== Campus Clash ===")
      campusOutput should include("Buildings:")

      countOccurrences(campusOutput, "s - Show status") shouldBe 1
      countOccurrences(campusOutput, "0 - End game") shouldBe 1
      countOccurrences(campusOutput, "> ") shouldBe 1

      val buildingOutput = runGame(Seq("1", "0"))

      buildingOutput should include("=== A Building ===")
      buildingOutput should include("Rooms:")
      buildingOutput should include("b - Leave building")

      countOccurrences(buildingOutput, "s - Show status") shouldBe 2
      countOccurrences(buildingOutput, "0 - End game") shouldBe 2
      countOccurrences(buildingOutput, "> ") shouldBe 2
    }

    "not show quests when a room has no quests" in {
      val output = runGame(Seq("1", "0"))

      output should include("Room: Entrance Hall")
      output should not include("Quests:")
    }

    "show status and reject invalid campus and building input" in {
      val output = runGame(
        Seq("s", "text", "99", "1", "s", "text", "99", "0")
      )

      output should include("=== Status ===")
      output should include("Name:  Student")
      output should include("HP:    100/100")
      output should include("Level: 1")

      countOccurrences(output, "Invalid input.") shouldBe 4
    }

    "allow leaving a building and ending the game on campus" in {
      val output = runGame(Seq("1", "b", "0"))

      countOccurrences(output, "Location: Campus") shouldBe 2
      output should include("Game ended.")
    }
  }