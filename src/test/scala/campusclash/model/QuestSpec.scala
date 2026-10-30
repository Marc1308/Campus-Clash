
package campusclash.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class QuestTest extends AnyWordSpec with Matchers:

  val quest = Quest(
    titel = "Bibliotheks-Quest",
    question = "Wie heißt die Hauptstadt von Deutschland?",
    correctAnswer = "Berlin"
  )

  "A Quest" should {

    "return true for a correct answer" in {
      quest.checkAnswer("Berlin") shouldBe true
    }

    "return false for an incorrect answer" in {
      quest.checkAnswer("München") shouldBe false
    }

    "ignore uppercase and lowercase letters" in {
      quest.checkAnswer("berlin") shouldBe true
    }

    "ignore whitespace at the beginning and end" in {
      quest.checkAnswer(" Berlin ") shouldBe true
    }

    "return false for an empty answer" in {
      quest.checkAnswer("") shouldBe false
    }
  }

