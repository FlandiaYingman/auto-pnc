package top.anagke.auto_pnc


import mu.KotlinLogging
import top.anagke.auto_android.AutoModule
import top.anagke.auto_android.Device
import top.anagke.auto_android.assert
import top.anagke.auto_android.img.Tmpl
import top.anagke.auto_android.nap
import top.anagke.auto_android.sleep
import top.anagke.auto_android.whileNotMatch

private val logger = KotlinLogging.logger {}

class SearchModule(
    private val config: AutoPncConfig,
    private val device: Device = config.emulator.open(),
) : AutoModule {

    companion object {
        val 检索指令数量不足: Tmpl by tmpl()

        @JvmStatic
        fun main(args: Array<String>) {
            SearchModule(AutoPncConfig.loadConfig()).auto()
        }
    }

    override fun run() = device.run {
        logger.info { "心智检索：开始" }
        assert(主界面)

        dragd(1240, 360, -400, 0).nap() //使“心智检索”可见
        tap(1157, 366).sleep() //心智检索
        dragd(715, 665, -1000, 0).nap() //使“基础检索”可见
        tap(715 - 1000, 665).nap() //心智检索
        tap(630, 665).nap() //基础检索

        tap(1165, 665).sleep() //十连
        whileNotMatch(检索指令数量不足) {
            tap(1183, 56).nap() //跳过
            tap(1183, 56).nap() //点击任意位置继续

            tap(1165, 665).sleep() //十连
        }
        tap(640, 520).nap() //确认“检索指令数量不足”

        tap(975, 665).sleep() //单抽
        whileNotMatch(检索指令数量不足) {
            tap(1183, 56).nap() //跳过
            tap(1183, 56).nap() //点击任意位置继续

            tap(975, 665).sleep() //单抽
        }
        tap(640, 520).nap() //确认“检索指令数量不足”

        jumpOut()
        logger.info { "心智检索：完成" }
    }

}