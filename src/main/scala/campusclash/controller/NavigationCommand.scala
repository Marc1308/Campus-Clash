package campusclash.controller

import campusclash.logic.GameLogic
import campusclash.model.{GameState, WorldData}

sealed trait NavigationCommand:

  private[controller] def doStep(
                                  state: GameState,
                                  world: WorldData
                                ): Option[GameState]

  private[controller] def undoStep(
                                    previousState: GameState
                                  ): GameState = previousState

  private[controller] def redoStep(
                                    nextState: GameState
                                  ): GameState = nextState


object NavigationCommand:

  case class EnterBuilding(buildingId: String)
    extends NavigationCommand:

    private[controller] override def doStep(
                                             state: GameState,
                                             world: WorldData
                                           ): Option[GameState] =
      GameLogic.findBuilding(world, buildingId)
        .map(GameLogic.enterBuilding(state, _))


  case class EnterRoom(roomId: String)
    extends NavigationCommand:

    private[controller] override def doStep(
                                             state: GameState,
                                             world: WorldData
                                           ): Option[GameState] =
      for
        building <- GameLogic.currentBuilding(state, world)
        room <- GameLogic.findRoom(building, roomId)
      yield GameLogic.enterRoom(state, room)


  case object LeaveBuilding extends NavigationCommand:

    private[controller] override def doStep(
                                             state: GameState,
                                             world: WorldData
                                           ): Option[GameState] =
      Some(GameLogic.leaveBuilding(state))