package campusclash.model

case class Quest(
                  id: String,
                  title: String,
                  description: String,
                  condition: QuestCondition
                )
