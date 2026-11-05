package campusclash.logic

import campusclash.model.*

object GameLogic:

  def findBuilding(worldData: WorldData, buildingId: String): Option[Building] =
    worldData.buildings.find(_.id == buildingId)

  def findRoom(building: Building, roomId: String): Option[Room] =
    building.rooms.find(_.id == roomId)

  def currentBuilding(
                       state: GameState,
                       worldData: WorldData
                     ): Option[Building] =
    state.currentBuildingId.flatMap(findBuilding(worldData, _))

  def currentRoom(
                   state: GameState,
                   worldData: WorldData
                 ): Option[Room] =
    currentBuilding(state, worldData).flatMap { building =>
      state.currentRoomId.flatMap(findRoom(building, _))
    }

  def enterBuilding(state: GameState, building: Building): GameState =
    state.copy(
      currentBuildingId = Some(building.id),
      currentRoomId = Some(building.entryRoomId)
    )

  def leaveBuilding(state: GameState): GameState =
    state.copy(
      currentBuildingId = None,
      currentRoomId = None
    )

  def enterRoom(state: GameState, room: Room): GameState =
    state.copy(currentRoomId = Some(room.id))

  def completeQuest(state: GameState, questId: String): GameState =
    state.copy(
      completedQuestIds = state.completedQuestIds + questId
    )

  def damagePlayer(
                    state: GameState,
                    amount: Int
                  ): GameState =
    state.copy(
      player = state.player.takeDamage(amount)
    )

  def healPlayer(
                  state: GameState,
                  amount: Int
                ): GameState =
    state.copy(
      player = state.player.heal(amount)
    )

  def isGameOver(state: GameState): Boolean =
    !state.player.isAlive
