package controllers.service

import com.dimafeng.testcontainers.{Container, FixedHostPortGenericContainer, ForAllTestContainer, ForEachTestContainer, GenericContainer, MySQLContainer}
import model.search.{All, TorrSearchRequest}
import model.torrent.{NotSetCategory, Torrent, TorrentFile}
import monix.eval.Task
import org.scalatest.{FlatSpec, Matchers}
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.elasticsearch.ElasticsearchContainer
import play.api.test.Helpers._
import play.api.test._
import service.EsDao
import monix.execution.Scheduler.Implicits.global

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 *
 * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
 */
class EsDaoSpec extends FlatSpec with GuiceFakeApplicationFactory with Matchers with ForEachTestContainer {

  val app = fakeApplication()

  override val container = FixedHostPortGenericContainer("docker.elastic.co/elasticsearch/elasticsearch:6.4.3",
    exposedHostPort = 9200,
    exposedContainerPort = 9200,
    env = Map("discovery.type" -> "single-node"),
    waitStrategy = Wait.forLogMessage(".*started.*", 1)
  )


  "EsDao" should "asdasd" in {
    val dao = app.injector.instanceOf[EsDao]

    dao.insert(Torrent(
      hash = "hash1",
      name = "name1",
      files = Seq(TorrentFile("/a/b/c/1.mp3", "mp3")),
      size = 1488,
      contentType = "music",
      contentCategory = NotSetCategory
    )).runSyncUnsafe()

    val result = dao.searchTorrents(TorrSearchRequest(
      text = "name1",
      contentCategory = None,
      contentType = None,
      mode = All,
      0,
      1000)).runSyncUnsafe()

    println("OKE")
  }


  "EsDao" should "search by name only" in {

  }

  "EsDao" should "search by file paths only" in {

  }

  def verify(insert: Seq[Torrent], req: TorrSearchRequest, verify: Seq[Torrent])(implicit dao: EsDao) {
    Task.sequence(insert.map(t => dao.insert(t))).runSyncUnsafe()
    val actual = dao.searchTorrents(req).runSyncUnsafe()
    actual should contain theSameElementsAs verify
  }

}
