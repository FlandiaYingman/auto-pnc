package top.anagke.auto_pnc.explore

import mu.KotlinLogging
import top.anagke.auto_android.device.*
import top.anagke.auto_android.img.Tmpl
import top.anagke.auto_android.util.Pos
import top.anagke.auto_android.util.minutes
import top.anagke.auto_pnc.*


class ExploreModule(auto: AutoPnc) : PncModule(auto) {

    companion object {
        private val logger = KotlinLogging.logger {}

        private val 准备作战: Tmpl by tmpl()
        private val 作战开始: Tmpl by tmpl()
        private val canAI: Tmpl by tmpl()
        private val 动态结算界面: Tmpl by tmpl()
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
            val auto = AutoPnc.default()
            ExploreModule(auto).farm漏洞排查()
        }
    }

    override val name = "探索模块"

    override fun run() {
        logger.info { "探索：开始" }
        enterExplore()
        farm漏洞排查()
        farm碎片搜索()
        farm算法采集()
        quitExplore()
        logger.info { "探索：完成" }
    }

    fun enterExplore() = device.apply {
        assert(主界面)
        tap(1085, 230) //探索
        await(可跳出)
    }

    fun repositionExplore() = device.apply {
        swipev(640, 360, 1280 * 3, 720 * 3, 10.0, description = "拖至左上角").nap()
        drag(640, 720, 16, 16, description = "拖到默认位置").nap()
    }

    fun farm碎片搜索() = device.apply {
        repositionExplore()
        logger.info { "探索：farm碎片搜素" }
        tapd(705, 80).sleep() //碎片搜索
        assert(可自动战斗, 可自动战斗_禁用)

        tap(76, 225).nap() //第一位人形
        if (match(可自动战斗)) {
            自动战斗(farmAll = true, epPos = Pos(1110, 492))
        }

        tap(76, 328).nap() //第二位人形
        if (match(可自动战斗)) {
            自动战斗(farmAll = true, epPos = Pos(1110, 492))
        }

        jumpBack()
        logger.info { "探索：完成farm碎片搜素" }
    }

    fun farm算法采集() = device.apply {
        repositionExplore()
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
            自动战斗(farmAll = true, epPos = Pos(1110, 492))
        }

        jumpBack()
        logger.info { "探索：完成farm算法采集" }
    }

    /**
     * 自动战斗兼容的副本。 目前支持的副本：
     * - 碎片搜索
     * - 资源收集
     * - 算法采集
     */
    private fun Device.自动战斗(farmAll: Boolean = false, epPos: Pos = Pos(386, 492)) {
        assert(可自动战斗, 可自动战斗_算法采集)
        tap(epPos).nap()
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

    fun farm漏洞排查() = device.apply {
        repositionExplore()
        tapd(142, 353, description = "点击漏洞排查").sleep()
        Vulnerability.apply {
            run(config.exploreConfig.vulnerabilityLevel, config.exploreConfig.vulnerabilityPlan)
        }
        jumpBack()
        jumpBack()
    }

    fun quitExplore() = device.apply {
        jumpOut()
    }

    fun farmActivity(levelPos: Pos = Pos(640, 360), useItem: Boolean) = device.apply {
        whileNotMatch(准备作战) {
            tap(levelPos, description = "点击副本").nap()
        }

        tap(1127, 636, description = "准备作战")
        await(作战开始)

        tap(1127, 636, description = "作战开始")
        farmAI()

    }

    fun farmAI(levelPos: Pos = Pos(640, 360)) = device.apply {
        whileNotMatch(canAI) {
            tap(200, 550, description = "开启计划模式").nap() //开启计划模式
        }

        tap(326, 547, description = "执行计划")
        await(动态结算界面, 作战成功, timeout = 5.minutes)

        if (matched(动态结算界面)) {
            tap(897, 627, description = "领取").sleep()
            await(作战成功, timeout = 5.minutes)
        }

        tap(1052, 623, description = "返回")
        sleep()
    }

}

fun main() {
    ExploreModule(AutoPnc.default()).apply {
        while (true) {
            farmActivity(useItem = true)
        }
    }
}