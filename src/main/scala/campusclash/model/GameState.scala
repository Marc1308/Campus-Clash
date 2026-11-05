package campusclash.model

case class GameState(
                      player: Player,
                      currentBuildingId: Option[String],
                      currentRoomId: Option[String],
                      completedQuestIds: Set[String]
                    )
