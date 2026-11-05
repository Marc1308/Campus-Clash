package campusclash.data

import campusclash.model.WorldData
import campusclash.data.WorldDataJson.given
import upickle.default.read

import scala.io.Source
import scala.util.Using

object WorldDataLoader:

  def load(): WorldData =
    loadResource("/data/buildings.json")

  private[data] def loadResource(resourcePath: String): WorldData =
    val stream =
      getClass.getResourceAsStream(resourcePath)

    if stream == null then
      throw new IllegalArgumentException(
        s"Resource $resourcePath not found"
      )

    val json =
      Using.resource(Source.fromInputStream(stream)) { source =>
        source.mkString
      }

    parse(json)

  def parse(json: String): WorldData =
    read[WorldData](json)
