package campusclash.model

case class Building(
                     id: String,
                     name: String,
                     description: String,
                     entryRoomId: String,
                     rooms: Seq[Room]
                   )
