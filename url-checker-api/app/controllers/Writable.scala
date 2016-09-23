package controllers

import data.HtmlStats
import play.api.http.{Writeable, ContentTypes, ContentTypeOf}
import play.api.libs.json.{Json, JsValue}
import play.api.mvc.Codec

import scala.concurrent.ExecutionContext.Implicits.global

object Writable {

  implicit val errorDtoFromat = Json.format[ErrorDto]
  implicit val htmlStatsFromat = Json.format[HtmlStats]

  private def generateWritable(jsValue: JsValue)(implicit codec: Codec): Array[Byte] = codec.encode(Json.stringify(jsValue))

  implicit def writeableOfHtmlStats(implicit codec: Codec, ct: ContentTypeOf[HtmlStats] = ContentTypeOf[HtmlStats](Some(ContentTypes.JSON))): Writeable[HtmlStats] = {
    Writeable(errorDTO => generateWritable(Json.toJson(errorDTO)))
  }

  implicit def writeableOfErrorDto(implicit codec: Codec, ct: ContentTypeOf[ErrorDto] = ContentTypeOf[ErrorDto](Some(ContentTypes.JSON))): Writeable[ErrorDto] = {
    Writeable(errorDTO => generateWritable(Json.toJson(errorDTO)))
  }
}
