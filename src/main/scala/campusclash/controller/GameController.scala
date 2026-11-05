package campusclash.controller

import campusclash.logic.GameLogic
import campusclash.model.*

final class GameController(
                            initialState: GameState,
                            worldData: WorldData
                          ):

  private var gameState = initialState
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
    GameLogic.findBuilding(worldData, buildingId) match
      case Some(building) =>
        updateState(GameLogic.enterBuilding(gameState, building))
        true
      case None =>
        false

  def enterRoom(roomId: String): Boolean =
    currentBuilding.flatMap(GameLogic.findRoom(_, roomId)) match
      case Some(room) =>
        updateState(GameLogic.enterRoom(gameState, room))
        true
      case None =>
        false

  def leaveBuilding(): Unit =
    updateState(GameLogic.leaveBuilding(gameState))

  def completeQuest(questId: String): Unit =
    updateState(GameLogic.completeQuest(gameState, questId))

  def damagePlayer(amount: Int): Unit =
    updateState(GameLogic.damagePlayer(gameState, amount))

  def healPlayer(amount: Int): Unit =
    updateState(GameLogic.healPlayer(gameState, amount))

  def isGameOver: Boolean =
    GameLogic.isGameOver(gameState)

  private def updateState(newState: GameState): Unit =
    gameState = newState
    observers.foreach(_.onGameStateChanged(gameState))
