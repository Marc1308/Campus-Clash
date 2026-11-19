package campusclash.data

import campusclash.model.QuestCondition

object QuestConditionFactory:

  def create(json: ujson.Value): QuestCondition =
    json("type").str match

      case "enterRoom" =>
        QuestCondition.EnterRoom(json("roomId").str)

      case "playerLevelAtLeast" =>
        QuestCondition.PlayerLevelAtLeast(json("level").num.toInt)

      case "enemyDefeated" =>
        QuestCondition.EnemyDefeated(json("enemyId").str)

      case "itemCollected" =>
        QuestCondition.ItemCollected(
          json("itemId").str,
          json("amount").num.toInt
        )

      case "questCompleted" =>
        QuestCondition.QuestCompleted(json("questId").str)

      case conditionType =>
        throw new IllegalArgumentException(
          s"Unknown quest condition type: $conditionType"
        )