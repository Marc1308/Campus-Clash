package campusclash.model

enum QuestCondition:

  case EnterRoom(roomId: String)

  case PlayerLevelAtLeast(level: Int)

  case EnemyDefeated(enemyId: String)

  case ItemCollected(
                      itemId: String,
                      amount: Int
                    )

  case QuestCompleted(questId: String)
