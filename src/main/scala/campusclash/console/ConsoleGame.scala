package campusclash.console

import campusclash.model.*
import scala.annotation.tailrec
import scala.io.StdIn.readLine

object ConsoleGame:

  def start(): Unit =
    val player = Player(
      name = "Student",
      health = 100,
      maxHealth = 100,
      level = 1
    )

    val initialState = GameState(
      player = player,
      currentLocation = Location.Campus
    )

    gameLoop(initialState)


  @tailrec
  private def gameLoop(state: GameState): Unit =
    println()
    println("=== Campus Clash ===")
    println(s"Ort: ${state.currentLocation.displayName}")
    println()

    state.currentLocation match

      case Location.Campus =>
        println("1 - A-Gebäude betreten")
        println("2 - Library betreten")
        println("3-  Mensa betreten")
        println("4-  Status anzeigen")
        println("0 - Spiel beenden")

        readLine("> ") match
          case "1" =>
            val newState =
              state.copy(currentLocation = Location.BuildingA)

            gameLoop(newState)

          case "2" =>
            val newState =
              state.copy(currentLocation = Location.Library)

            gameLoop(newState)

          case "3" =>
            val newState =
               state.copy(currentLocation = Location.Mensa)

            gameLoop(newState)

          case "4" =>
            showStatus(state.player)
            gameLoop(state)

          case "0" =>
            println("Spiel beendet.")

          case _ =>
            println("Ungültige Eingabe.")
            gameLoop(state)


      case Location.BuildingA =>
        println("1 - Gebäude verlassen")
        println("2 - Status anzeigen")
        println("0 - Spiel beenden")

        readLine("> ") match
          case "1" =>
            val newState =
              state.copy(currentLocation = Location.Campus)

            gameLoop(newState)

          case "2" =>
            showStatus(state.player)
            gameLoop(state)

          case "0" =>
            println("Spiel beendet.")

          case _ =>
            println("Ungültige Eingabe.")
            gameLoop(state)

      case Location.Library=>
        println("1 - Library verlassen")
        println("2 - Status anzeigen")
        println("0 - Spiel beenden")

        readLine("> ") match
          case "1" =>
            val newState =
              state.copy(currentLocation = Location.Campus)

            gameLoop(newState)

          case "2" =>
            showStatus(state.player)
            gameLoop(state)

          case "0" =>
            println("Spiel beendet.")

          case _ =>
            println("Ungültige Eingabe.")
            gameLoop(state)

      case Location.Mensa =>
        println("1 - Mensa verlassen")
        println("2 - Status anzeigen")
        println("0 - Spiel beenden")

        readLine("> ") match
          case "1" =>
            val newState =
              state.copy(currentLocation = Location.Campus)

            gameLoop(newState)

          case "2" =>
            showStatus(state.player)
            gameLoop(state)

          case "0" =>
            println("Spiel beendet.")

          case _ =>
            println("Ungültige Eingabe.")
            gameLoop(state)


  private def showStatus(player: Player): Unit =
    println()
    println("=== Status ===")
    println(s"Name:  ${player.name}")
    println(s"HP:    ${player.health}/${player.maxHealth}")
    println(s"Level: ${player.level}")