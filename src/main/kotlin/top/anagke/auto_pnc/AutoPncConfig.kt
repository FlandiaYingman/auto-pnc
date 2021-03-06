package top.anagke.auto_pnc

import com.sksamuel.hoplite.ConfigLoader
import top.anagke.auto_android.device.BlueStacksConf
import top.anagke.auto_pnc.explore.ExploreConfig
import java.nio.file.NoSuchFileException
import java.nio.file.Path
import kotlin.io.path.exists

data class AutoPncConfig(
    val cacheLocation: Path,
    val emulator: BlueStacksConf,
    val username: String,
    val password: String,
    val exploreConfig: ExploreConfig
) {

    companion object {

        private val baseConfigFiles = listOf(
            Path.of("./base-config.toml"),
            //...
        )

        private val defaultConfigFiles = listOf(
            Path.of("./config.toml"),
            //...
        )


        fun loadConfig(givenConfigFiles: List<Path> = emptyList()): AutoPncConfig {
            val baseConfigFile = findExisting(baseConfigFiles) ?: throw NoSuchFileException("$baseConfigFiles")
            val configFile = findExisting((givenConfigFiles + defaultConfigFiles))
            return ConfigLoader().loadConfigOrThrow(listOfNotNull(configFile?.toString(), baseConfigFile.toString()))
        }

        private fun findExisting(paths: List<Path>) = paths.find { it.exists() }

    }

}