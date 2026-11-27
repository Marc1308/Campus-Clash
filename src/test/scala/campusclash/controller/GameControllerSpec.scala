package campusclash.controller

import campusclash.model.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class GameControllerSpec extends AnyWordSpec with Matchers:

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

  private def newController(): GameController =
    new GameController(
      GameState(
        Player("Alex", 80, 100, 2),
        None,
        None,
        Set.empty
      ),
      worldData
    )

  private final class RecordingObserver extends GameObserver:
    var states: Vector[GameState] = Vector.empty

    override def onGameStateChanged(state: GameState): Unit =
      states = states :+ state

  "GameController" should {
    "expose available world data" in {
      val controller = newController()

      controller.buildings shouldBe Seq(building)
      controller.currentBuilding shouldBe None
      controller.currentRoom shouldBe None
    }

    "notify an observer initially and after state changes" in {
      val controller = newController()
      val observer = new RecordingObserver()

      controller.addObserver(observer)
      controller.addObserver(observer)
      controller.enterBuilding(building.id) shouldBe true
      controller.enterRoom(classroom.id) shouldBe true

      observer.states.size shouldBe 3
      observer.states.head.currentBuildingId shouldBe None
      observer.states.last.currentRoomId shouldBe Some(classroom.id)
      controller.currentBuilding shouldBe Some(building)
      controller.currentRoom shouldBe Some(classroom)
    }

    "stop notifying a removed observer" in {
      val controller = newController()
      val observer = new RecordingObserver()

      controller.addObserver(observer)
      controller.removeObserver(observer)
      controller.enterBuilding(building.id)

      observer.states.size shouldBe 1
    }

    "reject unknown building and room IDs" in {
      val controller = newController()

      controller.enterBuilding("missing") shouldBe false
      controller.enterRoom(classroom.id) shouldBe false
      controller.enterBuilding(building.id) shouldBe true
      controller.enterRoom("missing") shouldBe false
    }

    "execute the same room command against the current state" in {
      val controller = newController()
      val observer = new RecordingObserver()
      controller.addObserver(observer)
      val visitClassroom = NavigationCommand.EnterRoom(classroom.id)

      controller.execute(visitClassroom) shouldBe false
      observer.states shouldBe Vector(controller.state)

      controller.execute(NavigationCommand.EnterBuilding(building.id)) shouldBe true
      controller.execute(visitClassroom) shouldBe true
      observer.states.size shouldBe 3
      observer.states.last.currentRoomId shouldBe Some(classroom.id)
    }

    "leave the current building" in {
      val controller = newController()
      controller.enterBuilding(building.id)

      controller.leaveBuilding()

      controller.state.currentBuildingId shouldBe None
      controller.state.currentRoomId shouldBe None
    }

    "update quest and player state" in {
      val controller = newController()

      controller.completeQuest("quest1")
      controller.damagePlayer(80)

      controller.state.completedQuestIds shouldBe Set("quest1")
      controller.state.player.health shouldBe 0
      controller.isGameOver shouldBe true

      controller.healPlayer(20)

      controller.state.player.health shouldBe 20
      controller.isGameOver shouldBe false
    }
    "undo navigation actions" in {
      val controller = newController()

      controller.enterBuilding(building.id) shouldBe true
      controller.currentBuilding shouldBe Some(building)

      controller.undo() shouldBe true

      controller.currentBuilding shouldBe None
      controller.currentRoom shouldBe None
    }
    "redo undone navigation actions" in {
      val controller = newController()

      controller.enterBuilding(building.id)
      controller.undo()

      controller.redo() shouldBe true

      controller.currentBuilding shouldBe Some(building)
    }
  }
