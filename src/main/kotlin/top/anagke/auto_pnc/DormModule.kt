package top.anagke.auto_pnc

import org.tinylog.kotlin.Logger
import top.anagke.auto_android.device.*
import top.anagke.auto_android.img.Tmpl

class DormModule(auto: AutoPnc) : PncModule(auto) {

    companion object {
        private val 宿舍界面: Tmpl by tmpl()
        private val 宿舍界面_可收取: Tmpl by tmpl()
    }

    override val name = "宿舍模块"

    override fun run() {
        Logger.info { "宿舍：开始" }
        enterDorm()
        collectResources()
        exitDorm()
        Logger.info { "宿舍：结束" }
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