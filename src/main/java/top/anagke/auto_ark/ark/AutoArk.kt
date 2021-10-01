package top.anagke.auto_ark.ark

import mu.KotlinLogging
import top.anagke.auto_ark.adb.AndroidActivity
import top.anagke.auto_ark.adb.Device
import top.anagke.auto_ark.ark.login.ArkLogin
import top.anagke.auto_ark.ark.mission.ArkMission
import top.anagke.auto_ark.ark.operate.ArkOperate
import top.anagke.auto_ark.ark.recruit.ArkRecruit
import top.anagke.auto_ark.ark.riic.ArkRiic
import java.time.DayOfWeek
import java.time.LocalDateTime

class AutoArk(
    val config: AutoArkConfig,
    val device: Device = config.emulator.open(),
) {

    companion object {

        val log = KotlinLogging.logger {}

        val arkToday: DayOfWeek get() = LocalDateTime.now().minusHours(4).dayOfWeek

    }

    fun routine() {
        autoLogin()
        autoRecruit()
        autoOperate()
        autoRiic()
        autoMission()
    }


    fun autoLogin() = runModule {
        ArkLogin(device).auto()
    }

    fun autoRecruit() = runModule {
        ArkRecruit(device, config.recruitConfig).auto()
    }

    fun autoOperate() = runModule {
        ArkOperate(device, config.operateConfig).auto()
    }

    fun autoRiic() = runModule {
        ArkRiic(device).auto()
    }

    fun autoMission() = runModule {
        ArkMission(device).auto()
    }


    private fun runModule(block: () -> Unit) {
        try {
            block()
            saveAppConfig(config)
        } catch (e: Exception) {
            onModuleError(e)
        } finally {
            onModuleEnds()
        }
    }

    private fun onModuleEnds() = device.apply {
        saveAppConfig(config)
    }

    private fun onModuleError(e: Exception) = device.apply {
        log.error("错误发生，尝试退出到主界面", e)
        jumpOut()
    }

}

fun main() {
    AutoArk(appConfig).autoRecruit()
}