package service

import data.HtmlStats
import org.jsoup._
import org.jsoup.nodes.DocumentType
import org.jsoup.select.Elements
import play.api.Logger
import play.api.http.Status
import play.api.libs.ws.ning.NingWSClient

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import scala.util.Try

/**
 * Created by igusher on 8/25/16.
 */
class HtmlAnalyser {

  val logger = Logger(this.getClass)
  val wsClient = NingWSClient()

  /**
   * Analyse html document according to spec.
   * @param url
   * @return
   */
  def analyse(url: String): Future[HtmlStats] = {
    logger.trace(s"Entering analyse url=$url")
    wsClient.url(url).get().flatMap(response => {
     response.status match {
       case Status.OK => {
         val doc = Jsoup.parse(response.body)
         val links = doc.select("a[href]")
         val htmlVersion: String = this.getHtmlVersion(doc)
         val headings: Map[String, Int] = this.countHeadings(doc)
         val linksCount: Int = links.size()
         val title: String = this.getTitle(doc)
         val hasLoginForm: Boolean = this.hasLoginForm(doc)

         this.findInaccessibleLinks(links).map(links => {
           HtmlStats(htmlVersion, headings, linksCount, links, title, hasLoginForm)
         })
       }
       case _ => throw  new Exception(s"returned ${response.status}")
     }
    })
  }

  /**
   * Search for a title tag in html and return its contents
   * @param doc
   * @return contents of title tag
   */
  private[service] def getTitle(doc: org.jsoup.nodes.Document): String = doc.title()


  /**
   * Search for a heading tags (h1..h6) tag in html and count number of occurrence
   * @param doc
   * @return mapping from header to count of occurrence in page. e.g. "h1" -> 10
   */
  private[service] def countHeadings(doc: org.jsoup.nodes.Document): Map[String, Int] = {
    logger.trace("Entering countHeadings")
    doc.select("h1, h2, h3, h4, h5, h6")
      .toArray(new Array[org.jsoup.nodes.Element](0))
      .toSeq
      .groupBy(_.nodeName())
      .map(n => (n._1, n._2.length))
  }

  /**
   * Search for a doctype tag in html and extract html publicid from it
   * @param doc
   * @return
   */
  private[service] def getHtmlVersion(doc: org.jsoup.nodes.Document): String = {
    logger.trace("Entering getHtmlVersion")
    doc.childNodes()
      .toArray(new Array[org.jsoup.nodes.Node](0))
      .toSeq
      .find(_.isInstanceOf[DocumentType])
      .map(_.asInstanceOf[DocumentType].attr("publicid"))
      .getOrElse("N/A")
  }

  /**
   * Traverse trough the collection of links and search for inaccessible ones.
   * @param links
   * @return collection of inaccessible urls
   */
  private[service] def findInaccessibleLinks(links: Elements): Future[Seq[String]] = {
    logger.trace("Entering findInaccessibleLinks")
    Future.sequence(
      links.toArray(new Array[org.jsoup.nodes.Element](0))
        .toSeq
        .map(link => {
          val url = link.absUrl("href")
          isUrlAccessible(url)
            .map(isAvailabe => (isAvailabe, link.attr("href")))
      })
    ).map(ls => {
      val inaccessibleLinks = ls.filter(_._1 == false).map(_._2)
      inaccessibleLinks
    })
  }

  /**
   * Check if url is accessible asynchronously
   * @param url to test
   * @return
   */
  private[service] def isUrlAccessible(url: String): Future[Boolean] = {
    logger.trace(s"Entering isUrlAccessible url=$url")
    Try(wsClient.url(url).get().map(response => {
      response.status match {
        case Status.OK => true
        case _ => false
      }
    })).getOrElse(Future.successful(false))
  }

  /**
   * Check if url is accessible asynchronously
   * Basically, check if html has <input type='text'> with name passowrd
   * @param doc
   * @return
   */
  private[service] def hasLoginForm(doc: org.jsoup.nodes.Document): Boolean = {
    doc.select("input")
      .toArray(new Array[org.jsoup.nodes.Element](0))
      .toSeq
      .filter(_.attr("type") == "text")
      .exists(_.attr("name") == "password")
  }
}
