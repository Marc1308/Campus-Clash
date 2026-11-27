package campusclash

import campusclash.console.ConsoleGame
import campusclash.controller.GameController
import campusclash.data.WorldDataLoader
import campusclash.model.{GameState, Player}

import scala.util.{Success, Failure}

object Main:

  def main(args: Array[String]): Unit =

    WorldDataLoader.load() match

      case Success(worldData) =>
        val initialState = GameState(
          player = Player(
            name = "Student",
            health = 100,
            maxHealth = 100,
            level = 1
          ),
          currentBuildingId = None,
          currentRoomId = None,
          completedQuestIds = Set.empty
        )

        val controller = new GameController(initialState, worldData)
        val consoleView = new ConsoleGame(controller)

        consoleView.start()

      case Failure(error) =>
        println(s"Could not load game data: ${error.getMessage}")