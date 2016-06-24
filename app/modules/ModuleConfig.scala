package modules

import javax.inject.Singleton

import com.typesafe.config.Config

@Singleton
class ModuleConfig {

  def loadConfig() : Config = {
    val config = com.typesafe.config.ConfigFactory.parseFile(new java.io.File("/var/novapost/showcase/showcase.yml"))
    if(config.isEmpty) {
      throw new Exception("Module configuration not found")
    } else if (config.getString("env") != "valid") {
      throw new Exception("Module configuration not valid")
    }
    config
  }

  loadConfig()

}
