package top.anagke.auto_pnc.factory

import org.tinylog.kotlin.Logger
import top.anagke.auto_android.device.*
import top.anagke.auto_android.device.operation.whileFind
import top.anagke.auto_android.img.Pos
import top.anagke.auto_android.img.Tmpl
import top.anagke.auto_pnc.*


class FactoryModule(auto: AutoPnc) : PncModule(auto) {

    companion object {
        private val 加工厂界面: Tmpl by tmpl()
        private val 加工厂界面_可领取: Tmpl by tmpl(diff = 0.025)
        private val 加工厂界面_可加速: Tmpl by tmpl(diff = 0.025)
    }

    override val name = "加工厂模块"

    private enum class Room(val x: Int, val y: Int, val selector: TableSelector) {
        采掘矿场(
            497, 185,
            VerticalListSelector(
                Pos(1060, 195),
                Pos(1060, 645),
                111,
                127,
                5,
                10,
            )
        ),
        物资车间(
            789, 186,
            VerticalListSelector(
                Pos(1060, 195),
                Pos(1060, 645),
                111,
                127,
                5,
                10,
            )
        ),
        礼品工房(
            350, 357,
            VerticalListSelector(
                Pos(1060, 195),
                Pos(1060, 645),
                111,
                127,
                5,
                10,
            )
        ),
        数据封装中心(
            615, 366,
            VerticalListSelector(
                Pos(1060, 195),
                Pos(1060, 645),
                111,
                127,
                5,
                10,
            )
        )
    }

    override fun run() {
        Logger.info { "加工厂：开始" }
        enterFactory()
        collectTasks()
        submitTasks()
        exitFactory()
        Logger.info { "加工厂：完成" }
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
        room.selector.copy().apply {
            device.tapItem(fromSeqNum(itemIndexRange.random())).nap()
        }
        device.tap(762, 565).nap() //最多
        device.tap(723, 639).nap() //确认
        device.tapd(390, 691).nap() //确保返回到加工厂界面
    }

    private fun submitTask(room: Room, itemIndexRange: IntRange) = submitTask(room, itemIndexRange.toList())

    private fun submitTasks() {
        // 采掘矿场的生产取决于其它三个房间的生产。
        // 由于绿洲会产出底格币和预制件，所以采掘矿场不考虑它们。
        // 采掘矿场生产随机低模数据，为基础检索指令；生产中模三角和菱形数据，为技能枢核。
        submitTask(Room.采掘矿场, (2..4) + (6..7))

        // 物资车间只自动生产基础检索指令。算法相关的道具不消耗太多时间，可以手动生产并加速。
        submitTask(Room.物资车间, listOf(2))

        // 礼品工房生产橙色礼物，由于橙色礼物效率最高，且生产蓝色礼物会消耗本应留给技能枢核的低模数据。
        submitTask(Room.礼品工房, 16..23)

        // 只生产技能枢核
        submitTask(Room.数据封装中心, listOf(5)) //75% 技能枢核、25% 技能素材箱
    }

    private fun exitFactory() {
        device.assert(加工厂界面)
        device.jumpOut()
    }

}