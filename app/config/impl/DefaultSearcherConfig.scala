package config.impl

import java.net.URL

import com.google.inject.Inject
import com.typesafe.scalalogging.LazyLogging
import config.SearcherConfig
import javax.inject.Singleton
import play.api.Configuration

@Singleton
class DefaultSearcherConfig @Inject()(conf: Configuration) extends SearcherConfig with LazyLogging {
  override val esEndpoint: URL = new URL(conf.get[String]("es.endpoint"))
}
