package top.anagke.auto_pnc

import org.tinylog.kotlin.Logger
import top.anagke.auto_android.device.assert
import top.anagke.auto_android.device.await
import top.anagke.auto_android.device.match
import top.anagke.auto_android.device.nap
import top.anagke.auto_android.img.Tmpl


class OasisModule(auto: AutoPnc) : PncModule(auto) {

    companion object {
        private val 绿洲界面: Tmpl by tmpl()
        private val 绿洲界面_可收取: Tmpl by tmpl()
    }

    override val name = "绿洲模块"

    override fun run() {
        Logger.info { "绿洲：开始" }
        enterOasis()
        collectResources()
        exitOasis()
        Logger.info { "绿洲：结束" }
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