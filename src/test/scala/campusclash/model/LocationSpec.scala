package campusclash.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class LocationSpec extends AnyWordSpec with Matchers:

  "Location.Campus" should {
    "have the correct display name" in {
      Location.Campus.displayName shouldBe "Campus"
    }
  }

  "Location.BuildingA" should {
    "have the correct display name" in {
      Location.BuildingA.displayName shouldBe "A-Gebaeude"
    }
  }

  "Location.Library" should {
    "have the correct display name" in {
      Location.Library.displayName shouldBe "Library"
    }
  }

  "All locations" should {
    "have a non-empty display name" in {
      Location.values.foreach { location =>
        location.displayName should not be empty
      }
    }

    "have unique display names" in {
      val names = Location.values.map(_.displayName)
      names.distinct.length shouldBe names.length
    }
  }