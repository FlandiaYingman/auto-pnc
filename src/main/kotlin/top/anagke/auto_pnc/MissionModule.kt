package top.anagke.auto_pnc

import mu.KotlinLogging
import top.anagke.auto_android.device.*
import top.anagke.auto_android.img.Tmpl

private val logger = KotlinLogging.logger {}

class MissionModule(auto: AutoPnc) : PncModule(auto) {

    companion object {
        private val 主界面_可完成任务: Tmpl by tmpl()
    }

    override val name = "任务模块"

    override fun run() {
        logger.info { "任务：开始" }
        升级人形()
        完成任务()
        logger.info { "任务：完成" }
    }

    private fun 升级人形() = device.run {
        //TODO: 替换为刷罗萨姆的无尽勘探
        logger.info { "任务：升级人形：开始" }
        device.assert(主界面)
        tap(1244, 194).sleep()
        dragv(1083, 665, 0, -2160)
        tap(181, 561).nap()
        tap(181, 561).nap()
        tap(1128, 485).nap()
        jumpOut()
        logger.info { "任务：升级人形：完成" }
    }

    private fun 完成任务() {
        device.assert(主界面)
        device.whileMatch(主界面_可完成任务) {
            device.tap(323, 465) //完成任务
            device.tap(323, 465)
            device.tap(323, 465).sleep() //完成任务
        }
        if (device.match(可跳出)) {
            device.jumpOut()
        }
    }

}