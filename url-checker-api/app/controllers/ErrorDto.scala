package controllers

import play.api.libs.json.{Json, JsValue}
import Writable._

/**
 * Created by igusher on 8/25/16.
 */
case class ErrorDto(`type`: String, status: Int, title: String, detail: String, instance: String) {
  def toJson: JsValue = Json.toJson(this)
}

