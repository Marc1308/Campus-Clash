package campusclash.console

import campusclash.controller.{GameController, GameObserver, NavigationCommand}
import campusclash.controller.NavigationCommand.{EnterBuilding, EnterRoom, LeaveBuilding}
import campusclash.model.*

import scala.annotation.tailrec
import scala.io.StdIn.readLine

final class ConsoleGame(controller: GameController) extends GameObserver:

  private sealed trait ConsoleScreen:
    def render(): Unit
    def navigationFor(input: String): Option[NavigationCommand]

  private final class CommonActionsScreen(screen: ConsoleScreen) extends ConsoleScreen:
    override def render(): Unit =
      screen.render()
      println("s - Show status")
      println("0 - End game")

    override def navigationFor(input: String): Option[NavigationCommand] =
      screen.navigationFor(input)

  private object CampusScreen extends ConsoleScreen:
    override def render(): Unit = showCampus()

    override def navigationFor(input: String): Option[NavigationCommand] =
      selectByNumber(input, controller.buildings).map(building => EnterBuilding(building.id))

  private final class BuildingScreen(building: Building) extends ConsoleScreen:
    override def render(): Unit =
      showBuildingInfo(building)
      controller.currentRoom.foreach(showRoomInfo)
      showBuildingMenu(building)

    override def navigationFor(input: String): Option[NavigationCommand] =
      input match
        case "b" => Some(LeaveBuilding)
        case number => selectByNumber(number, building.rooms).map(room => EnterRoom(room.id))

  def start(): Unit =
    controller.addObserver(this)
    try gameLoop()
    finally controller.removeObserver(this)

  override def onGameStateChanged(state: GameState): Unit =
    currentScreen.render()

  private def currentScreen: ConsoleScreen =
    val screen = controller.currentBuilding match
      case None => CampusScreen
      case Some(building) => new BuildingScreen(building)

    new CommonActionsScreen(screen)

  @tailrec
  private def gameLoop(): Unit =
    val screen = currentScreen
    readLine("> ") match
      case "0" | null =>
        println("Game ended.")
      case "s" =>
        showStatus(controller.state.player)
        screen.render()
        gameLoop()
      case input =>
        if !screen.navigationFor(input).exists(controller.execute) then
          println("Invalid input.")
          screen.render()
        gameLoop()

  private def selectByNumber[A](input: String, values: Seq[A]): Option[A] =
    input.toIntOption
      .filter(index => index >= 1 && index <= values.size)
      .map(index => values(index - 1))

  private def showCampus(): Unit =
    println()
    println("=== Campus Clash ===")
    println("Location: Campus")
    println()
    println("Buildings:")
    controller.buildings.zipWithIndex.foreach { case (building, index) =>
      println(s"${index + 1} - ${building.name}")
    }

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

  private def showStatus(player: Player): Unit =
    println()
    println("=== Status ===")
    println(s"Name:  ${player.name}")
    println(s"HP:    ${player.health}/${player.maxHealth}")
    println(s"Level: ${player.level}")
