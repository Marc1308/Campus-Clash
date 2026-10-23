package campusclash.model

enum Location(val displayName: String):
  case Campus extends Location("Campus")
  case BuildingA extends Location("A-Gebäude")
  case Library extends Location("Libary")
  case Mensa extends Location ("Mensa")