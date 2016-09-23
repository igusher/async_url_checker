import javax.inject._

import controllers.ErrorDto
import play.api._
import play.api.http.ContentTypes._
import play.api.http.DefaultHttpErrorHandler
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc.Results._
import play.api.mvc._
import play.api.routing.Router
import scala.concurrent._

class ErrorHandler @Inject()(env: Environment,
                              config: Configuration,
                              sourceMapper: OptionalSourceMapper,
                              router: Provider[Router]
                              ) extends DefaultHttpErrorHandler(env, config, sourceMapper, router) {

  private val logger = Logger(this.getClass)

  override def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = {

    val response = exception match {
      case e: Exception => {
        logger.error("Uncaught exception.", e)
        if (e.getCause != null)
          logger.error("Cause.", e.getCause)
        InternalServerError(ErrorDto("InternalServerError", 500, "InternalServerError", "", "").toJson).as(JSON)
      }
    }
    Future(response)
  }
}
