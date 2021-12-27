package top.anagke.auto_pnc

import com.charleskorn.kaml.Yaml
import com.sksamuel.hoplite.ConfigLoader
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import mu.KotlinLogging
import top.anagke.auto_android.AutoModule
import top.anagke.auto_android.Device
import top.anagke.auto_android.Emulator
import top.anagke.auto_android.assert
import top.anagke.auto_android.await
import top.anagke.auto_android.img.Img
import top.anagke.auto_android.img.Tmpl
import top.anagke.auto_android.matched
import top.anagke.auto_android.sleep
import top.anagke.auto_android.util.hours
import java.nio.file.NoSuchFileException
import java.nio.file.Path
import kotlin.concurrent.thread
import kotlin.io.path.exists
import kotlin.io.path.notExists
import kotlin.io.path.readText
import kotlin.io.path.writeText
import kotlin.reflect.KProperty

private val logger = KotlinLogging.logger {}

private val maxTries = 2
private val timeout = 1.hours


fun tmpl(diff: Double = 0.05) = TmplDelegate(diff)

class TmplDelegate(private val diff: Double) {
    private var tmpl: Tmpl? = null
    operator fun getValue(thisRef: Any?, property: KProperty<*>): Tmpl {
        if (tmpl == null) {
            val name = "${property.name}.png"
            val kClass = thisRef?.let { it::class.java } ?: AutoPnc::class.java
            val tmplBytes = kClass
                .getResource(name)!!
                .readBytes()
            tmpl = Tmpl(name, diff, Img.decode(tmplBytes)!!)
        }
        return tmpl!!
    }
}


// 主界面
val 主界面: Tmpl by tmpl()
// 主界面
val 主界面_拖出: Tmpl by tmpl()
// 可跳回主界面
val 可跳出: Tmpl by tmpl()

fun Device.jumpOut() {
    assert(可跳出)
    tap(250, 50)
    await(主界面, 主界面_拖出)
    if (matched(主界面_拖出)) {
        dragd(840, 360, 400, 0).sleep() //使“心智检索”可见
    }
    await(主界面)
}

fun Device.jumpBack() {
    assert(可跳出)
    tap(120, 50).sleep()
}


class AutoPnc(
    config: AutoPncConfig = AutoPncConfig.loadConfig(),
    device: Device = config.emulator.open(),
) : AutoCloseable {

    private val initModule: AutoModule = LoginModule(config, device)

    private val modules: List<AutoModule> = listOf(
        DormModule(config, device),
        OasisModule(config, device),
        FactoryModule(config, device),
        SearchModule(config, device),
        ExploreModule(config, device),
        MissionModule(config, device),
        ExploreModule(config, device),
    )

    private fun initGame() {
        check(open)
        for (tries in 1..maxTries) {
            try {
                initModule.auto()
                return
            } catch (e: Exception) {
                if (e is InterruptedException) throw e
                if (tries != maxTries) {
                    logger.warn(e) { "运行初始模块 ${initModule.name} 时发生错误。已尝试：$tries/$maxTries 次；重新尝试……" }
                } else {
                    logger.error(e) { "运行初始模块 ${initModule.name} 时发生错误。已尝试：$tries/$maxTries 次；无法继续运行，退出。" }
                    this.close()
                }
            }
        }

    }

    private fun runModule(module: AutoModule) {
        check(open)
        for (tries in 1..maxTries) {
            try {
                module.auto()
                break
            } catch (e: Exception) {
                if (e is InterruptedException) throw e
                if (tries != maxTries) {
                    logger.warn(e) { "运行模块 ${module.name} 时发生错误。已尝试：$tries/$maxTries 次；重新尝试……" }
                } else {
                    logger.error(e) { "运行模块 ${module.name} 时发生错误。已尝试：$tries/$maxTries 次；无法继续运行该模块。" }
                }
                initGame()
            }
        }
    }


    /**
     * Automates PNC.
     */
    fun auto() {
        check(open)
        val runner = thread {
            try {
                this.run()
            } catch (e: InterruptedException) {
                logger.warn { "自动云图：终止。" }
            }
        }
        runner.join(timeout)
        if (runner.isAlive) {
            logger.warn { "自动云图已运行 $timeout ms，超时，尝试终止。" }
            runner.interrupt()
        }
    }

    fun run() {
        initGame()
        modules.forEach(this::runModule)
    }

    private var open = true
    override fun close() {
        open = false
    }

}


data class AutoPncConfig(
    val cacheLocation: Path,
    val emulator: Emulator,
    val username: String,
    val password: String,
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
            return ConfigLoader().loadConfigOrThrow(listOfNotNull(configFile, baseConfigFile))
        }

        private fun findExisting(paths: List<Path>) = paths.find { it.exists() }

    }

}

@Serializable
data class AutoPncModule(
    var farmingPlan: Map<String, Int> = mapOf(),
) {

    companion object {

        fun loadCache(cacheFile: Path): AutoPncModule {
            if (cacheFile.notExists()) saveCache(cacheFile, AutoPncModule())
            return Yaml.default.decodeFromString(cacheFile.readText())
        }

        fun saveCache(cacheFile: Path, cache: AutoPncModule) {
            cacheFile.writeText(Yaml.default.encodeToString(cache))
        }

    }

}