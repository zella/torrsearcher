package config.impl

import com.google.inject.{Inject, Singleton}
import config.OnStartupService
import monix.execution.Scheduler
import play.api.inject.ApplicationLifecycle

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

@Singleton
class DefaultOnStartupService @Inject()(lifecycle: ApplicationLifecycle)(implicit ec: Scheduler)
  extends OnStartupService {



}
