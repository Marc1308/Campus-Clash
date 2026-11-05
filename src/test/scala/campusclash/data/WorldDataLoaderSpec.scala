package campusclash.data

import campusclash.model.QuestCondition
import campusclash.data.WorldDataJson.given
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import upickle.default.{read, write}

class WorldDataLoaderSpec extends AnyWordSpec with Matchers:

  "WorldDataLoader" should {
    "parse valid building data" in {
      val json =
        """
          |{
          |  "buildings": [
          |    {
          |      "id": "buildingA",
          |      "name": "A Building",
          |      "description": "Test building",
          |      "entryRoomId": "a101",
          |      "rooms": [
          |        {
          |          "id": "a101",
          |          "name": "Room A101",
          |          "description": "Test room",
          |          "quests": [
          |            {
          |              "id": "visitA101",
          |              "title": "Visit A101",
          |              "description": "Reach the room.",
          |              "condition": {
          |                "type": "enterRoom",
          |                "roomId": "a101"
          |              }
          |            }
          |          ]
          |        }
          |      ]
          |    }
          |  ]
          |}
          |""".stripMargin

      val worldData = WorldDataLoader.parse(json)
      val building = worldData.buildings.head
      val room = building.rooms.head
      val quest = room.quests.head

      building.id shouldBe "buildingA"
      building.name shouldBe "A Building"
      building.description shouldBe "Test building"
      building.entryRoomId shouldBe "a101"
      room.id shouldBe "a101"
      room.name shouldBe "Room A101"
      room.description shouldBe "Test room"
      quest.id shouldBe "visitA101"
      quest.title shouldBe "Visit A101"
      quest.description shouldBe "Reach the room."
      quest.condition shouldBe QuestCondition.EnterRoom("a101")
    }

    "load the bundled buildings resource" in {
      val worldData = WorldDataLoader.load()

      worldData.buildings.map(_.id) should contain allOf ("buildingA", "library")
    }

    "report a missing resource" in {
      val exception = intercept[IllegalArgumentException] {
        WorldDataLoader.loadResource("/data/missing.json")
      }

      exception.getMessage should include("/data/missing.json")
    }

    "round-trip every supported quest condition" in {
      val conditions = Seq(
        QuestCondition.EnterRoom("a101"),
        QuestCondition.PlayerLevelAtLeast(2),
        QuestCondition.EnemyDefeated("enemy1"),
        QuestCondition.ItemCollected("book", 3),
        QuestCondition.QuestCompleted("quest1")
      )

      conditions.foreach { condition =>
        read[QuestCondition](write(condition)) shouldBe condition
      }
    }

    "reject an unknown quest condition type" in {
      val exception = intercept[Exception] {
        read[QuestCondition]("""{"type":"unknown"}""")
      }

      exception.getCause shouldBe a[IllegalArgumentException]
      exception.getCause.getMessage should include("Unknown quest condition type")
    }
  }
