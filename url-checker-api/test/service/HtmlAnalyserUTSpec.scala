package service

import org.jsoup.Jsoup
import org.scalatest.WordSpec

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
/**
 * Created by igusher on 8/26/16.
 */
class HtmlAnalyserUTSpec extends WordSpec {
  val htmlAnalyser = new HtmlAnalyser

  "HtmlAnalyser" should {
    "return N/A if no Doctype node was found in html" in {
      val html = "<html></html>"
      val doc = Jsoup.parse(html)
      assert(htmlAnalyser.getHtmlVersion(doc) === "N/A")

    }

    "Correctly detect inaccessible links" in {
      val html = "<html><a href='inaccessible'/> <a href=\"http://google.com\"/></html>"
      val doc = Jsoup.parse(html)
      val links = doc.select("a[href]")
      val brokenlinks = Await.result(htmlAnalyser.findInaccessibleLinks(links), 5 second)
      assert(brokenlinks === Seq("inaccessible"))
    }

    "Extract page title" in {
      val html = "<html><head><title>123</title></head></html>"
      val doc = Jsoup.parse(html)
      assert(htmlAnalyser.getTitle(doc) === "123")
    }

    "Return empty title if there is none on page" in {
      val html = "<html></html>"
      val doc = Jsoup.parse(html)
      assert(htmlAnalyser.getTitle(doc) === "")
    }

    "check login form" in {
      val html = "<html><body><input type=\"text\" name=\"password\"/></body></html>"
      val doc = Jsoup.parse(html)
      assert(htmlAnalyser.hasLoginForm(doc))
    }
  }
}
