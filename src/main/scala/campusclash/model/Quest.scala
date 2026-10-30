package campusclash.model

case class Quest (
                   titel: String,
                   question: String,
                   correctAnswer: String

                 ):
  def checkAnswer(answer:String):Boolean = 
  answer.trim.equalsIgnoreCase(correctAnswer)
