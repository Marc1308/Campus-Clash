package campusclash.data

import campusclash.model.WorldData
import campusclash.data.WorldDataJson.given
import upickle.default.read

import scala.io.Source
import scala.util.{Try, Failure, Using}

object WorldDataLoader:

  def load(): Try[WorldData] =
    loadResource("/data/buildings.json")

  private[data] def loadResource(resourcePath: String): Try[WorldData] =
    val stream =
      Option(getClass.getResourceAsStream(resourcePath))

    stream match
      case None =>
        Failure(
          new IllegalArgumentException(
            s"Resource $resourcePath not found"
          )
        )

      case Some(inputStream) =>
        Using(Source.fromInputStream(inputStream)) { source =>
          source.mkString
        }.flatMap(parse)
  def parse(json: String): Try[WorldData] =
    Try(read[WorldData](json))