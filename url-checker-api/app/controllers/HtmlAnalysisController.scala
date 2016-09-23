package controllers

import com.google.inject.{Singleton}
import data.HtmlStats
import play.api.libs.json.Json
import play.api.mvc._
import service.HtmlAnalyser
import scala.concurrent.ExecutionContext.Implicits.global
import Writable._

import org.apache.commons.validator.routines.UrlValidator

import scala.concurrent.Future

@Singleton
class HtmlAnalysisController extends Controller {

  val logger =  play.api.Logger(this.getClass)
  val urlValidator = new UrlValidator()
  val htmlAnalyser = new HtmlAnalyser
  implicit val htmlStatsFromat = Json.format[HtmlStats]

  def listAnalysisSuites = Action.async { implicit  request =>
    val optUrl = getQueryParameters("url").map(_.head)
    optUrl match {
      case None => Future.successful(BadRequest("Missing parameter 'url'"))
      case Some(url) if !urlValidator.isValid(url) => Future.successful(BadRequest("Invalid parameter 'url'"))
      case Some(url) => {
        htmlAnalyser.analyse(optUrl.get).map(stats =>{
          play.api.mvc.Results.Ok(stats).as(JSON)
        }).recover({case e: Exception =>
          play.api.mvc.Results.BadRequest(ErrorDto("", 400, "", e.getMessage, "")).as(JSON)
        })
      }
    }

  }
}
