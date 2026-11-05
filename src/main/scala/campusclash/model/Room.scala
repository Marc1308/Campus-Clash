package campusclash.model

case class Room(
                 id: String,
                 name: String,
                 description: String,
                 quests: Seq[Quest]
               )
