package si.budimir.discordHooks.config

import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.config.Configuration
import net.md_5.bungee.config.ConfigurationProvider
import java.io.File

import java.io.IOException

import net.md_5.bungee.config.YamlConfiguration

import java.io.InputStream
import java.nio.file.Files

open class ConfigBase(plugin: Plugin, _fileName: String) {
    private val main: Plugin = plugin

    private var configurationProvider: ConfigurationProvider = ConfigurationProvider.getProvider(YamlConfiguration::class.java)
    private var mainConfig: Configuration? = null
    private var configFile: File? = null
    private val fileName: String = _fileName
    private val logger = main.logger

    fun reloadConfig(): Boolean {
        try{
            if (!main.dataFolder.exists() || !configFile!!.exists()){
                logger.warning("Config file not found, making a new one")
                saveDefaultConfig()
            }

            if (configFile == null){
                logger.warning("CONFIG NULL")
                configFile = File(main.dataFolder, fileName)
            }

            mainConfig = configurationProvider.load(configFile)
            return true
        }catch (e: IOException){
            logger.severe("Failed to reload config!")
            return false
        }
    }

    protected fun getConfig(): Configuration? {
        if (mainConfig == null) reloadConfig()
        return mainConfig
    }

    private fun saveDefaultConfig() {
        if (!main.dataFolder.exists())
            main.dataFolder.mkdir();

        if (configFile == null)
            configFile = File(main.dataFolder, fileName)

        if (!configFile!!.exists()) {
            val defaultStream: InputStream = main.getResourceAsStream(fileName)
            val file = File(main.dataFolder, "config.yml")
            Files.copy(defaultStream, file.toPath())
        }
    }

    init {
        saveDefaultConfig()
    }
}