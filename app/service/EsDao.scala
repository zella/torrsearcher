package service

import com.google.inject.{Inject, Singleton}
import com.sksamuel.elastic4s.http.ElasticDsl._
import com.sksamuel.elastic4s.http.index.IndexResponse
import com.sksamuel.elastic4s.http.{ElasticClient, Response}
import com.sksamuel.elastic4s.playjson._
import com.sksamuel.elastic4s.searches.queries.NoopQuery
import com.sksamuel.elastic4s.{Hit, HitReader}
import model.search.{All, FilesOnly, NamesOnly, TorrSearchRequest}
import model.torrent.Torrent
import monix.eval.Task

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

@Singleton
class EsDao @Inject()(client: ElasticClient)(implicit ec: ExecutionContext) {

  //TODO Fields to constants

  def insert(torrent: Torrent): Task[Response[IndexResponse]] = {
    Task.deferFuture(client.execute {
      indexInto("torrsearcher" / "torrents").doc(torrent)
    })
  }

  def searchTorrents(req: TorrSearchRequest): Task[IndexedSeq[Torrent]] = {
    Task.deferFuture(client.execute {
      search("torrsearcher" / "torrents").query {
        val text = Some(req.mode match {
          case FilesOnly => matchQuery("files.path", req.text)
          case NamesOnly => matchQuery("name", req.text)
          case All => boolQuery().should(matchQuery("name", req.text), matchQuery("files.path", req.text))
        })
        val categoryOpt = req.contentCategory match {
          case Some(c) => Some(matchQuery("contentCategory", c.value))
          case None => None
        }
        val contentTypeOpt = req.contentType match {
          case Some(t) => Some(termQuery("contentCategory", t))
          case None => None
        }
        boolQuery().must(Seq(text, categoryOpt, contentTypeOpt).flatten: _*)
      }.from(req.skip)
        .size(req.limit)
        .timeout(60.seconds) //TODO conf
    }.map(r => r.result.to[Torrent]))
  }

}

