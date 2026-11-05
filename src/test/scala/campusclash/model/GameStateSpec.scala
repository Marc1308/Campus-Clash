package campusclash.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class GameStateSpec extends AnyWordSpec with Matchers:

  "A GameState" should {
    "store the complete game progress" in {
      val player = Player("Alex", 80, 100, 2)
      val state = GameState(
        player = player,
        currentBuildingId = Some("library"),
        currentRoomId = Some("studyRoom"),
        completedQuestIds = Set("firstQuest")
      )

      state.player shouldBe player
      state.currentBuildingId shouldBe Some("library")
      state.currentRoomId shouldBe Some("studyRoom")
      state.completedQuestIds shouldBe Set("firstQuest")
    }

    "represent the campus without a building or room" in {
      val state = GameState(
        Player("Alex", 100, 100, 1),
        currentBuildingId = None,
        currentRoomId = None,
        completedQuestIds = Set.empty
      )

      state.currentBuildingId shouldBe None
      state.currentRoomId shouldBe None
    }
  }
