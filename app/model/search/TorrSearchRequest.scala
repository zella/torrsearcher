package model.search

import model.torrent.ContentCategory

case class TorrSearchRequest(text: String,
                             contentCategory: Option[ContentCategory],
                             contentType: Option[Seq[String]],
                             mode: SearchMode,
                             skip: Int,
                             limit: Int
                            ) {

}

sealed trait SearchMode

case object FilesOnly extends SearchMode

case object NamesOnly extends SearchMode

case object All extends SearchMode

