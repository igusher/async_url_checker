import data.HtmlStats
import play.api.libs.json.Json
import play.api.mvc.{Request, AnyContent}

package object controllers {

  def getQueryParameters(query: String)(implicit request: Request[AnyContent]): Option[Seq[String]] = {
    request.queryString.map(x => (x._1.toLowerCase, x._2)).get(query.toLowerCase)
  }
}
