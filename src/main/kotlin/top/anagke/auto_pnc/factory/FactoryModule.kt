package top.anagke.auto_pnc.factory

import top.anagke.auto_android.AutoModule
import top.anagke.auto_android.Device
import top.anagke.auto_android.assert
import top.anagke.auto_android.await
import top.anagke.auto_android.img.Tmpl
import top.anagke.auto_android.nap
import top.anagke.auto_android.sleep
import top.anagke.auto_android.tapdListItem
import top.anagke.auto_android.whileMatch
import top.anagke.auto_pnc.AutoPncConfig
import top.anagke.auto_pnc.jumpOut
import top.anagke.auto_pnc.template
import top.anagke.auto_pnc.主界面

class FactoryModule(
    private val device: Device,
) : AutoModule {

    companion object {
        private val 加工厂界面: Tmpl = template("factory/加工厂界面.png")
        private val 加工厂界面_可领取: Tmpl = template("factory/加工厂界面_可领取.png")
        private val 加工厂界面_可加速: Tmpl = template("factory/加工厂界面_可加速.png")

        @JvmStatic
        fun main(args: Array<String>) {
            FactoryModule(AutoPncConfig.loadConfig().emulator.open()).auto()
        }
    }

    private enum class Room(val x: Int, val y: Int) {
        采掘矿场(497, 185),
        物资车间(789, 186),
        礼品工房(350, 357),
        数据封装中心(615, 366)
    }

    override fun run() {
        enterFactory()
        collectTasks()
        submitTasks()
        exitFactory()
    }

    private fun enterFactory() {
        device.assert(主界面)
        device.tap(1166, 595) //加工厂
        device.await(加工厂界面)
    }

    private fun collectTasks() {
        device.tap(127, 652).nap()
        device.whileMatch(加工厂界面_可领取) {
            device.tap(941, 178).sleep() //收取资源
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

    private fun submitTask(room: Room, itemIndexRange: IntRange) = itemIndexRange.toList()

    private fun submitTasks() {
        submitTask(Room.采掘矿场, 2..10) //随机的“……数据”
        device.tap(762, 565).nap() //最多
        device.tap(723, 639).nap() //确认
        device.tapd(390, 691).nap() //确保返回到加工厂界面

        submitTask(Room.物资车间, (1..2) + (9..9)) //“作战经验”或“基础检索指令”或“算法素材箱”
        device.tap(762, 565).nap() //最多
        device.tap(723, 639).nap() //确认
        device.tapd(390, 691).nap() //确保返回到加工厂界面

        submitTask(Room.礼品工房, 0..7) //蓝色礼物
        device.tap(762, 565).nap() //最多
        device.tap(723, 639).nap() //确认
        device.tapd(390, 691).nap() //确保返回到加工厂界面

        submitTask(Room.数据封装中心, 6..6) //蓝色礼物
        device.tap(762, 565).nap() //最多
        device.tap(723, 639).nap() //确认
        device.tapd(390, 691).nap() //确保返回到加工厂界面
    }

    private fun exitFactory() {
        device.assert(加工厂界面)
        device.jumpOut()
    }

}