package top.anagke.auto_pnc

import mu.KotlinLogging
import top.anagke.auto_android.*
import top.anagke.auto_android.img.Tmpl
import top.anagke.auto_android.util.minutes

private val logger = KotlinLogging.logger {}

class ExploreModule(
    private val config: AutoPncConfig,
    private val device: Device = findEmulator(config.emulators),
) : AutoModule {

    companion object {

        private val canFarmAI: Tmpl by tmpl()
        private val atPrepareScreen: Tmpl by tmpl()
        private val canAI: Tmpl by tmpl()
        private val 作战成功: Tmpl by tmpl()

        private val 可自动战斗: Tmpl by tmpl()
        private val 可自动战斗_禁用: Tmpl by tmpl()
        private val 可自动战斗_算法采集: Tmpl by tmpl()
        private val 可自动战斗_禁用_算法采集: Tmpl by tmpl()
        private val 自动战斗结果: Tmpl by tmpl()
        private val 获得奖励: Tmpl by tmpl()

        private val 算法采集区域2_已折叠 by tmpl()
        private val 算法采集区域2_未折叠 by tmpl()

        @JvmStatic
        fun main(args: Array<String>) {
            ExploreModule(AutoPncConfig.loadConfig()).auto()
        }
    }

    override fun run() {
        logger.info { "探索：开始" }
        enterExplore()
        farm碎片搜索()
        repositionExplore()
        farm算法采集()
        quitExplore()
        logger.info { "探索：完成" }
    }

    fun enterExplore() = device.apply {
        assert(主界面)
        tap(1085, 230) //探索
        await(可跳出)
        repositionExplore()
    }

    fun repositionExplore() = device.apply {
        drag(16, 64, 1280, 720).nap() //拖到左上角
        drag(16, 64, 1280, 720).nap() //拖到左上角
        drag(640, 720, 16, 16).nap() //拖到默认位置
    }

    fun farm碎片搜索() = device.apply {
        logger.info { "探索：farm碎片搜素" }
        tapd(705, 80).sleep() //碎片搜索
        assert(可自动战斗, 可自动战斗_禁用)

        tap(76, 225).nap() //第一位人形
        if (match(可自动战斗)) {
            自动战斗(farmAll = true, epX = 1110)
        }

        tap(76, 328).nap() //第二位人形
        if (match(可自动战斗)) {
            自动战斗(farmAll = true, epX = 1110)
        }

        jumpBack()
        logger.info { "探索：完成farm碎片搜素" }
    }

    fun farm算法采集() = device.apply {
        logger.info { "探索：farm算法采集" }
        tapd(722, 224).sleep() //算法采集
        assert(可自动战斗_算法采集, 可自动战斗_禁用_算法采集)
        when (which(算法采集区域2_未折叠, 算法采集区域2_已折叠)) {
            算法采集区域2_未折叠 -> {
                tap(165, 470).nap()
            }
            算法采集区域2_已折叠 -> {
                tap(175, 235).nap()
            }
        }
        if (match(可自动战斗_算法采集)) {
            自动战斗(farmAll = true, epX = 1110)
        }

        jumpBack()
        logger.info { "探索：完成farm算法采集" }
    }

    private fun Device.自动战斗(farmAll: Boolean = false, epX: Int = 386) {
        assert(可自动战斗, 可自动战斗_算法采集)
        tap(epX, 492).nap()
        tap(858, 622).sleep() //自动战斗
        if (farmAll) {
            drag(462, 400, 816, 400).sleep()
        }
        tap(807, 519).sleep() //准备作战
        tap(1102, 648).sleep() //自动战斗
        await(自动战斗结果, timeout = 10.minutes)
        tap(670, 495).sleep() //完成
        whileMatch(获得奖励) {
            tap(670, 495).sleep() //完成
        }
    }

    fun quitExplore() = device.apply {
        jumpOut()
    }


    fun farmAI() = device.apply {
        await(canFarmAI)
        tap(1127, 636) //准备作战

        await(atPrepareScreen)
        tap(1127, 636) //作战开始

        whileNotMatch(canAI) {
            tap(200, 550).nap() //开启计划模式
        }
        tap(326, 547) //执行计划

        await(作战成功, timeout = 5.minutes)
        tap(1052, 623) //返回
        sleep()
    }

}

fun main() {
    ExploreModule(AutoPncConfig.loadConfig(), Device()).farm算法采集()
}