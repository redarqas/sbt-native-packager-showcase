package modules

import com.google.inject.AbstractModule

class StartUpModule extends AbstractModule {
  override def configure(): Unit = bind(classOf[ModuleConfig]).asEagerSingleton()
}

