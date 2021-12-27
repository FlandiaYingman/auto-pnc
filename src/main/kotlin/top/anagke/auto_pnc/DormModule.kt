package top.anagke.auto_pnc

import mu.KotlinLogging
import top.anagke.auto_android.AutoModule
import top.anagke.auto_android.Device
import top.anagke.auto_android.assert
import top.anagke.auto_android.await
import top.anagke.auto_android.img.Tmpl
import top.anagke.auto_android.match
import top.anagke.auto_android.nap
import top.anagke.auto_android.sleep

private val logger = KotlinLogging.logger {}

class DormModule(
    private val config: AutoPncConfig,
    private val device: Device = config.emulator.open(),
) : AutoModule {

    companion object {
        private val 宿舍界面: Tmpl by tmpl()
        private val 宿舍界面_可收取: Tmpl by tmpl()

        @JvmStatic
        fun main(args: Array<String>) {
            DormModule(AutoPncConfig.loadConfig()).auto()
        }
    }

    override fun run() {
        logger.info { "宿舍：开始" }
        enterDorm()
        collectResources()
        exitDorm()
        logger.info { "宿舍：结束" }
    }

    private fun enterDorm() {
        device.assert(主界面)
        device.tap(980, 590) //宿舍
        device.await(宿舍界面)
    }

    private fun collectResources() {
        device.assert(宿舍界面)
        sleep()
        if (device.match(宿舍界面_可收取)) {
            device.tap(125, 550).nap() //收取资源
            device.tap(125, 550).nap() //收取资源
        }
    }

    private fun exitDorm() {
        device.assert(宿舍界面)
        device.jumpOut()
    }

}