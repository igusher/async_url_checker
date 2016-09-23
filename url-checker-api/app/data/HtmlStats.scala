package data

/**
 * Created by igusher on 8/25/16.
 */

case class HtmlStats(val htmlVersion: String,
                     val headings: Map[String, Int],
                     val linksCount: Int,
                     val inaccessibleLinks: Seq[String],
                     val title: String,
                     val hasLoginForm: Boolean) {

}
