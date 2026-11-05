package campusclash.logic

import campusclash.model.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class GameLogicSpec extends AnyWordSpec with Matchers:

  private val entrance = Room(
    "entrance",
    "Entrance",
    "Building entrance",
    Seq.empty
  )
  private val classroom = Room(
    "classroom",
    "Classroom",
    "A classroom",
    Seq.empty
  )
  private val building = Building(
    "building",
    "Building",
    "Test building",
    entrance.id,
    Seq(entrance, classroom)
  )
  private val worldData = WorldData(Seq(building))
  private val campusState = GameState(
    Player("Alex", 80, 100, 2),
    None,
    None,
    Set.empty
  )

  "GameLogic navigation" should {
    "find buildings and rooms by ID" in {
      GameLogic.findBuilding(worldData, building.id) shouldBe Some(building)
      GameLogic.findBuilding(worldData, "missing") shouldBe None
      GameLogic.findRoom(building, classroom.id) shouldBe Some(classroom)
      GameLogic.findRoom(building, "missing") shouldBe None
    }

    "enter a building through its entry room" in {
      val state = GameLogic.enterBuilding(campusState, building)

      state.currentBuildingId shouldBe Some(building.id)
      state.currentRoomId shouldBe Some(entrance.id)
      GameLogic.currentBuilding(state, worldData) shouldBe Some(building)
      GameLogic.currentRoom(state, worldData) shouldBe Some(entrance)
    }

    "enter another room" in {
      val state = GameLogic.enterRoom(
        GameLogic.enterBuilding(campusState, building),
        classroom
      )

      GameLogic.currentRoom(state, worldData) shouldBe Some(classroom)
    }

    "leave a building" in {
      val state = GameLogic.leaveBuilding(
        GameLogic.enterBuilding(campusState, building)
      )

      state.currentBuildingId shouldBe None
      state.currentRoomId shouldBe None
      GameLogic.currentBuilding(state, worldData) shouldBe None
      GameLogic.currentRoom(state, worldData) shouldBe None
    }

    "return no room for an unknown room ID" in {
      val state = GameLogic.enterBuilding(campusState, building)
        .copy(currentRoomId = Some("missing"))

      GameLogic.currentRoom(state, worldData) shouldBe None
    }
  }

  "GameLogic quest progress" should {
    "record a completed quest without duplicates" in {
      val once = GameLogic.completeQuest(campusState, "quest1")
      val twice = GameLogic.completeQuest(once, "quest1")

      twice.completedQuestIds shouldBe Set("quest1")
    }
  }

  "GameLogic player actions" should {
    "damage and heal the player" in {
      val damaged = GameLogic.damagePlayer(campusState, 30)
      val healed = GameLogic.healPlayer(damaged, 10)

      damaged.player.health shouldBe 50
      healed.player.health shouldBe 60
    }

    "detect whether the game is over" in {
      GameLogic.isGameOver(campusState) shouldBe false
      GameLogic.isGameOver(GameLogic.damagePlayer(campusState, 80)) shouldBe true
    }
  }
