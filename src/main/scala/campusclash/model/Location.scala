package campusclash.model

enum Location(val displayName: String):
  case Campus extends Location("Campus")
  case BuildingA extends Location("A-Gebäude")