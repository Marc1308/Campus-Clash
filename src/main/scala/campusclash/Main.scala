package campusclash


import campusclash.console.ConsoleGame
import campusclash.controller.GameController
import campusclash.data.WorldDataLoader
import campusclash.model.{GameState, Player}

object Main:

  def main(args: Array[String]): Unit =
    val worldData = WorldDataLoader.load()
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
