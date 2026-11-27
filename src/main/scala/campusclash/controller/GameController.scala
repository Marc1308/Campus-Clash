package campusclash.controller

import campusclash.logic.GameLogic
import campusclash.model.*

final class GameController(
                            initialState: GameState,
                            worldData: WorldData
                          ):

  private var gameState = initialState
  private val undoManager = new UndoManager
  private var observers = Vector.empty[GameObserver]

  def state: GameState = gameState

  def buildings: Seq[Building] = worldData.buildings

  def currentBuilding: Option[Building] =
    GameLogic.currentBuilding(gameState, worldData)

  def currentRoom: Option[Room] =
    GameLogic.currentRoom(gameState, worldData)

  def addObserver(observer: GameObserver): Unit =
    if !observers.contains(observer) then
      observers = observers :+ observer
      observer.onGameStateChanged(gameState)

  def removeObserver(observer: GameObserver): Unit =
    observers = observers.filterNot(_ == observer)

  def enterBuilding(buildingId: String): Boolean =
    execute(NavigationCommand.EnterBuilding(buildingId))

  def enterRoom(roomId: String): Boolean =
    execute(NavigationCommand.EnterRoom(roomId))

  def leaveBuilding(): Unit =
    execute(NavigationCommand.LeaveBuilding)

  def execute(command: NavigationCommand): Boolean =
    command.doStep(gameState, worldData) match
      case Some(nextState) =>
        undoManager.execute(command, gameState, nextState)
        updateState(nextState)
        true
      case None =>
        false

  def undo(): Boolean =
    undoManager.undo() match
      case Some(previousState) =>
        updateState(previousState)
        true

      case None =>
        false

  def redo(): Boolean =
    undoManager.redo() match
      case Some(nextState) =>
        updateState(nextState)
        true

      case None =>
        false

  def completeQuest(questId: String): Unit =
    executeStateChange(
      GameLogic.completeQuest(gameState, questId)
    )

  def damagePlayer(amount: Int): Unit =
    executeStateChange(
      GameLogic.damagePlayer(gameState, amount)
    )

  def healPlayer(amount: Int): Unit =
    executeStateChange(
      GameLogic.healPlayer(gameState, amount)
    )

  def isGameOver: Boolean =
    GameLogic.isGameOver(gameState)

  private def updateState(newState: GameState): Unit =
    gameState = newState
    observers.foreach(_.onGameStateChanged(gameState))

  private def executeStateChange(nextState: GameState): Unit =
    if nextState != gameState then
      undoManager.execute(
        NavigationCommand.LeaveBuilding,
        gameState,
        nextState
      )
      updateState(nextState)
