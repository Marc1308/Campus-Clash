package campusclash.model

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class LocationSpec extends AnyFlatSpec with Matchers:

  "Location.Campus" should "have the correct display name" in {
    Location.Campus.displayName shouldBe "Campus"
  }

  "Location.BuildingA" should "have the correct display name" in {
    Location.BuildingA.displayName shouldBe "A-Gebäude"
  }

  "Location.Library" should "have the correct display name" in {
    Location.Library.displayName shouldBe "Library"
  }

