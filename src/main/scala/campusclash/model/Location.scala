package campusclash.model

enum Location(val displayName: String):
  case Campus extends Location("Campus")
  case BuildingA extends Location("A-Gebaeude")
  case Library extends Location("Library")
  case Mensa extends Location ("Mensa")