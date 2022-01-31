package top.anagke.auto_pnc

import mu.KotlinLogging
import top.anagke.auto_android.device.await
import top.anagke.auto_android.device.findEdge
import top.anagke.auto_android.device.nap
import top.anagke.auto_android.device.whileNotMatch
import top.anagke.auto_android.img.Tmpl

private val logger = KotlinLogging.logger {}

class LoginModule(auto: AutoPnc) : PncModule(auto) {

    companion object {
        private val 登录界面: Tmpl by tmpl()
        private val 主界面_可退出: Tmpl by tmpl(0.15)
    }

    override val name = "登录模块"

    override fun run() {
        logger.info { "登录：开始" }
        launchPnc()
        login()
        logger.info { "登录：完成" }
    }

    private fun launchPnc() {
        device.stop(PNC_ACTIVITY)
        device.launch(PNC_ACTIVITY)
    }

    private fun login() {
        logger.info { "登录：用户“${config.username}”" }
        device.await(登录界面)
//        device.tap(640, 600) //开始游戏
        device.tap(1200, 95).nap() //切换用户

        device.tapl(508, 280).nap() //选择用户名
        device.input(config.username).nap()

        device.tapl(570, 342).nap() //选择密码
        device.inputSecret(config.password).nap()

        device.tap(826, 411) //登录
        repeat(2) {
            device.whileNotMatch(主界面) {
                val pos = device.findEdge(主界面_可退出)
                if (pos != null) device.tap(pos.x, pos.y).nap()
                else device.tap(1260, 700)
            }
        }
    }

}