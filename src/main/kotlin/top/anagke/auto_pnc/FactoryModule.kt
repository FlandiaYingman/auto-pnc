package top.anagke.auto_pnc

import mu.KotlinLogging
import top.anagke.auto_android.*
import top.anagke.auto_android.img.Tmpl

private val logger = KotlinLogging.logger {}

class FactoryModule(
    private val config: AutoPncConfig,
    private val device: Device = config.emulator.open(),
) : AutoModule {

    companion object {
        private val 加工厂界面: Tmpl by tmpl()
        private val 加工厂界面_可领取: Tmpl by tmpl(diff = 0.025)
        private val 加工厂界面_可加速: Tmpl by tmpl(diff = 0.025)

        @JvmStatic
        fun main(args: Array<String>) {
            FactoryModule(AutoPncConfig.loadConfig()).auto()
        }
    }

    private enum class Room(val x: Int, val y: Int) {
        采掘矿场(497, 185),
        物资车间(789, 186),
        礼品工房(350, 357),
        数据封装中心(615, 366)
    }

    override fun run() {
        logger.info { "加工厂：开始" }
        enterFactory()
        collectTasks()
        submitTasks()
        exitFactory()
        logger.info { "加工厂：完成" }
    }

    private fun enterFactory() {
        device.assert(主界面)
        device.tap(1166, 595) //加工厂
        device.await(加工厂界面)
    }

    private fun collectTasks() {
        device.tap(127, 652).nap()

        device.whileFind(加工厂界面_可领取) {
            device.tap(it.x, it.y).sleep() //收取资源
            device.tap(1049, 95).nap() //确认收取
        }
        device.whileFind(加工厂界面_可加速) {
            device.tap(it.x, it.y).sleep() //收取资源
            device.tap(1049, 95).nap() //确认收取
        }
        device.tap(1049, 95).nap()
    }

    private fun submitTask(room: Room, itemIndexRange: List<Int>) {
        device.tap(room.x, room.y).nap() //指定房间
        device.tapdListItem(itemIndexRange.random(), 5, 1080, 190, 128, 110).nap()
        device.tap(762, 565).nap() //最多
        device.tap(723, 639).nap() //确认
        device.tapd(390, 691).nap() //确保返回到加工厂界面
    }

    private fun submitTask(room: Room, itemIndexRange: IntRange) = submitTask(room, itemIndexRange.toList())

    private fun submitTasks() {
        submitTask(Room.采掘矿场, 2..10) //随机的“……数据”
        submitTask(Room.物资车间, (1..2) + (9..9)) //“作战经验”或“基础检索指令”或“算法素材箱”
        submitTask(Room.礼品工房, 0..7) //蓝色礼物
        submitTask(Room.数据封装中心, listOf(5, 5, 5, 6)) //75% 技能枢核、25% 技能素材箱
    }

    private fun exitFactory() {
        device.assert(加工厂界面)
        device.jumpOut()
    }

}