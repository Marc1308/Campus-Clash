package campusclash.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class QuestSpec extends AnyWordSpec with Matchers:

  "A Quest" should {
    "store its condition" in {
      val quest = Quest(
        id = "visitA101",
        title = "Visit A101",
        description = "Reach Room A101.",
        condition = QuestCondition.EnterRoom("a101")
      )

      quest.condition shouldBe QuestCondition.EnterRoom("a101")
    }
  }
