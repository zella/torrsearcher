package model.torrent

import java.time.Instant

import play.api.libs.json.{Format, JsResult, JsString, JsSuccess, JsValue, Json}

case class Torrent(hash: String,
                   name: String,
                   files: Seq[TorrentFile],
                   size: Long,
                   contentType: String,
                   contentCategory: ContentCategory,
                   info: Option[String] = None,
                   seeders: Option[Int] = None,
                   leechers: Option[Int] = None,
                   added: Option[Instant] = None,
                   completed: Option[Boolean] = None,
                   good: Option[Int] = None,
                   bad: Option[Int] = None,
                   tracker: Option[String] = None
                  )

object Torrent {
  implicit val jsonFormat: Format[Torrent] = Json.format[Torrent]
}

case class TorrentFile(path: String, extension: String)

object TorrentFile {
  implicit val jsonFormat: Format[TorrentFile] = Json.format[TorrentFile]
}

sealed trait ContentCategory{
  def value:String
}

object ContentCategory {

  implicit val jsonFormat: Format[ContentCategory] = new Format[ContentCategory] {
    override def writes(o: ContentCategory): JsValue = {
      o match {
        case NotSetCategory => JsString("N/A")
        case FilledCategory(v) => JsString(v)
      }
    }

    override def reads(json: JsValue): JsResult[ContentCategory] = {
      val v = json.as[String]
      JsSuccess(v.toUpperCase match {
        case "N/A" => NotSetCategory
        case _ => FilledCategory(v)
      })
    }
  }
}

case object NotSetCategory extends ContentCategory {
  override def value: String = "N/A"
}

case class FilledCategory(value: String) extends ContentCategory