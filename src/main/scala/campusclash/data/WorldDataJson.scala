package campusclash.data

import campusclash.model.*
import upickle.default.{ReadWriter, macroRW, readwriter}

private[data] object WorldDataJson:

  given ReadWriter[QuestCondition] =
    readwriter[ujson.Value].bimap[QuestCondition](
      {
        case QuestCondition.EnterRoom(roomId) =>
          ujson.Obj("type" -> "enterRoom", "roomId" -> roomId)
        case QuestCondition.PlayerLevelAtLeast(level) =>
          ujson.Obj("type" -> "playerLevelAtLeast", "level" -> level)
        case QuestCondition.EnemyDefeated(enemyId) =>
          ujson.Obj("type" -> "enemyDefeated", "enemyId" -> enemyId)
        case QuestCondition.ItemCollected(itemId, amount) =>
          ujson.Obj(
            "type" -> "itemCollected",
            "itemId" -> itemId,
            "amount" -> amount
          )
        case QuestCondition.QuestCompleted(questId) =>
          ujson.Obj("type" -> "questCompleted", "questId" -> questId)
      },
      json =>
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
    )

  given ReadWriter[Quest] = macroRW
  given ReadWriter[Room] = macroRW
  given ReadWriter[Building] = macroRW
  given ReadWriter[WorldData] = macroRW
