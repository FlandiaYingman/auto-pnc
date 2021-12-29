package top.anagke.auto_pnc

import mu.KotlinLogging
import top.anagke.auto_android.AutoModule
import top.anagke.auto_android.Device
import top.anagke.auto_android.assert
import top.anagke.auto_android.await
import top.anagke.auto_android.img.Tmpl
import top.anagke.auto_android.match
import top.anagke.auto_android.nap

private val logger = KotlinLogging.logger {}

class OasisModule(
    private val config: AutoPncConfig,
    private val device: Device = config.emulator.open(),
) : AutoModule {

    companion object {
        private val 绿洲界面: Tmpl by tmpl()
        private val 绿洲界面_可收取: Tmpl by tmpl()

        @JvmStatic
        fun main(args: Array<String>) {
            OasisModule(AutoPncConfig.loadConfig()).auto()
        }
    }

    override fun run() {
        logger.info { "绿洲：开始" }
        enterOasis()
        collectResources()
        exitOasis()
        logger.info { "绿洲：结束" }
    }

    private fun enterOasis() {
        device.assert(主界面)
        device.tap(928, 417) //绿洲
        device.await(绿洲界面)
    }

    private fun collectResources() {
        if (device.match(绿洲界面_可收取)) {
            device.tap(84, 565).nap() //收取资源
            device.tap(84, 565).nap() //收取资源
        }
    }

    private fun exitOasis() {
        device.assert(绿洲界面)
        device.jumpOut()
    }

}