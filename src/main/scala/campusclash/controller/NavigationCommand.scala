package campusclash.controller

import campusclash.logic.GameLogic
import campusclash.model.{GameState, WorldData}

sealed trait NavigationCommand:

  private[controller] def transition(state: GameState, world: WorldData): Option[GameState]

object NavigationCommand:

  final case class EnterBuilding(buildingId: String) extends NavigationCommand:
    private[controller] override def transition(state: GameState, world: WorldData): Option[GameState] =
      GameLogic.findBuilding(world, buildingId).map(GameLogic.enterBuilding(state, _))

  final case class EnterRoom(roomId: String) extends NavigationCommand:
    private[controller] override def transition(state: GameState, world: WorldData): Option[GameState] =
      for
        building <- GameLogic.currentBuilding(state, world)
        room <- GameLogic.findRoom(building, roomId)
      yield GameLogic.enterRoom(state, room)

  case object LeaveBuilding extends NavigationCommand:
    private[controller] override def transition(state: GameState, world: WorldData): Option[GameState] =
      Some(GameLogic.leaveBuilding(state))
