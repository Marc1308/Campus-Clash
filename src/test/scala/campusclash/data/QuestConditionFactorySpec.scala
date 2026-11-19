package campusclash.data

import campusclash.model.QuestCondition
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class QuestConditionFactorySpec extends AnyWordSpec with Matchers:

  "QuestConditionFactory" should {

    "create an EnterRoom condition" in {
      val json = ujson.Obj(
        "type" -> "enterRoom",
        "roomId" -> "room1"
      )

      QuestConditionFactory.create(json) shouldBe
        QuestCondition.EnterRoom("room1")
    }

    "create a PlayerLevelAtLeast condition" in {
      val json = ujson.Obj(
        "type" -> "playerLevelAtLeast",
        "level" -> 5
      )

      QuestConditionFactory.create(json) shouldBe
        QuestCondition.PlayerLevelAtLeast(5)
    }

    "create an EnemyDefeated condition" in {
      val json = ujson.Obj(
        "type" -> "enemyDefeated",
        "enemyId" -> "enemy1"
      )

      QuestConditionFactory.create(json) shouldBe
        QuestCondition.EnemyDefeated("enemy1")
    }

    "create an ItemCollected condition" in {
      val json = ujson.Obj(
        "type" -> "itemCollected",
        "itemId" -> "sword",
        "amount" -> 2
      )

      QuestConditionFactory.create(json) shouldBe
        QuestCondition.ItemCollected("sword", 2)
    }

    "create a QuestCompleted condition" in {
      val json = ujson.Obj(
        "type" -> "questCompleted",
        "questId" -> "quest1"
      )

      QuestConditionFactory.create(json) shouldBe
        QuestCondition.QuestCompleted("quest1")
    }

    "throw an exception for an unknown condition type" in {
      val json = ujson.Obj(
        "type" -> "unknown"
      )

      intercept[IllegalArgumentException] {
        QuestConditionFactory.create(json)
      }
    }
  }
