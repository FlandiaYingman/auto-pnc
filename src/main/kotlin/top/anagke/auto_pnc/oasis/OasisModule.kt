package top.anagke.auto_pnc.oasis

import top.anagke.auto_android.AutoModule
import top.anagke.auto_android.Device
import top.anagke.auto_android.assert
import top.anagke.auto_android.await
import top.anagke.auto_android.img.Tmpl
import top.anagke.auto_android.match
import top.anagke.auto_android.nap
import top.anagke.auto_pnc.AutoPncConfig
import top.anagke.auto_pnc.jumpOut
import top.anagke.auto_pnc.template
import top.anagke.auto_pnc.主界面

class OasisModule(
    private val device: Device,
) : AutoModule {

    companion object {
        private val 绿洲界面: Tmpl = template("oasis/绿洲界面.png")
        private val 绿洲界面_可收取: Tmpl = template("oasis/绿洲界面_可收取.png")

        @JvmStatic
        fun main(args: Array<String>) {
            OasisModule(AutoPncConfig.loadConfig().emulator.open()).auto()
        }
    }

    override fun run() {
        enterOasis()
        collectResources()
        exitOasis()
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