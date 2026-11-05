package campusclash.console

import campusclash.controller.{GameController, GameObserver}
import campusclash.model.*

import scala.annotation.tailrec
import scala.io.StdIn.readLine

final class ConsoleGame(controller: GameController) extends GameObserver:

  def start(): Unit =
    controller.addObserver(this)
    gameLoop()
    controller.removeObserver(this)

  override def onGameStateChanged(state: GameState): Unit =
    render(state)

  @tailrec
  private def gameLoop(): Unit =
    val continue =
      controller.currentBuilding match
        case None => handleCampusInput()
        case Some(building) => handleBuildingInput(building)

    if continue then gameLoop()

  private def handleCampusInput(): Boolean =
    readLine("> ") match
      case "0" =>
        println("Game ended.")
        false
      case "s" =>
        showStatus(controller.state.player)
        render(controller.state)
        true
      case input =>
        selectByNumber(input, controller.buildings) match
          case Some(building) =>
            controller.enterBuilding(building.id)
            true
          case None =>
            println("Invalid input.")
            render(controller.state)
            true

  private def handleBuildingInput(building: Building): Boolean =
    readLine("> ") match
      case "0" =>
        println("Game ended.")
        false
      case "b" =>
        controller.leaveBuilding()
        true
      case "s" =>
        showStatus(controller.state.player)
        render(controller.state)
        true
      case input =>
        selectByNumber(input, building.rooms) match
          case Some(room) =>
            controller.enterRoom(room.id)
            true
          case None =>
            println("Invalid input.")
            render(controller.state)
            true

  private def selectByNumber[A](input: String, values: Seq[A]): Option[A] =
    input.toIntOption
      .filter(index => index >= 1 && index <= values.size)
      .map(index => values(index - 1))

  private def render(state: GameState): Unit =
    state.currentBuildingId.flatMap { _ => controller.currentBuilding } match
      case Some(building) =>
        showBuildingInfo(building)
        controller.currentRoom.foreach(showRoomInfo)
        showBuildingMenu(building)
      case None =>
        showCampus()

  private def showCampus(): Unit =
    println()
    println("=== Campus Clash ===")
    println("Location: Campus")
    println()
    println("Buildings:")
    controller.buildings.zipWithIndex.foreach { case (building, index) =>
      println(s"${index + 1} - ${building.name}")
    }
    println("s - Show status")
    println("0 - End game")

  private def showBuildingInfo(building: Building): Unit =
    println()
    println(s"=== ${building.name} ===")
    println(building.description)

  private def showRoomInfo(room: Room): Unit =
    println()
    println(s"Room: ${room.name}")
    println(room.description)

    if room.quests.nonEmpty then
      println("Quests:")
      room.quests.foreach { quest =>
        println(s"- ${quest.title}: ${quest.description}")
        println(s"  Condition: ${quest.condition}")
      }

  private def showBuildingMenu(building: Building): Unit =
    println()
    println("Rooms:")
    building.rooms.zipWithIndex.foreach { case (room, index) =>
      println(s"${index + 1} - ${room.name}")
    }
    println("b - Leave building")
    println("s - Show status")
    println("0 - End game")

  private def showStatus(player: Player): Unit =
    println()
    println("=== Status ===")
    println(s"Name:  ${player.name}")
    println(s"HP:    ${player.health}/${player.maxHealth}")
    println(s"Level: ${player.level}")
