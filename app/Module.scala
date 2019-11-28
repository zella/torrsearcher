import com.google.inject.{AbstractModule, Provides, Singleton}
import com.sksamuel.elastic4s.http.{ElasticClient, ElasticProperties}
import config.SearcherConfig
import config.impl.DefaultSearcherConfig
import monix.execution.Scheduler
import play.api.{Configuration, Environment}

class Module extends AbstractModule {

  @Provides
  @Singleton
  def scheduler(): Scheduler = monix.execution.Scheduler.global

  @Provides
  @Singleton
  def config(conf: Configuration): SearcherConfig = new DefaultSearcherConfig(conf)

  @Provides
  @Singleton
  def esClient(conf: SearcherConfig): ElasticClient = ElasticClient(ElasticProperties(conf.esEndpoint.toExternalForm))

}
