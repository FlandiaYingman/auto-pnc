package top.anagke.auto_ark.ark.operate

import top.anagke.auto_ark.adb.Device
import top.anagke.auto_ark.adb.sleep

class OperateLevel(
    val name: String,
    val timeout: Long = 5L * 60L * 1000L,
    val enter: Device.() -> Unit,
)

//作战记录
val LS_5 = OperateLevel("LS-5（作战记录）") {
    tap(970, 203).sleep() //终端
    tap(822, 670).sleep() //资源收集
    tap(643, 363).sleep() //战术演习
    tap(945, 177).sleep() //LS-5
}

//龙门币
val CE_5 = OperateLevel("CE-5（龙门币）") {
    tap(970, 203).sleep() //终端
    tap(822, 670).sleep() //资源收集
    tap(438, 349).sleep()
    tap(945, 177).sleep() //CE-5
}

//技巧概要
val CA_5 = OperateLevel("CA-5（技巧概要）") {
    tap(970, 203).sleep() //终端
    tap(822, 670).sleep() //资源收集
    tap(229, 357).sleep()
    tap(945, 177).sleep() //CA-5
}

//当期剿灭作战
val annihilation = OperateLevel("剿灭作战", timeout = 30L * 60L * 1000L) {
    tap(970, 203).sleep() //终端
    tap(1000, 665).sleep()
    tap(835, 400).sleep()
}

val PR_A_2 = OperateLevel("PR-X-2（重装/医疗芯片）") {
    tap(970, 203).sleep() //终端
    tap(822, 670).sleep() //Resource Collection
    tap(842, 329).sleep()
    tap(830, 258).sleep() //PR-X-2
}

val PR_XX_2 = OperateLevel("PR-X-2（狙击/术士芯片）") {
    tap(970, 203).sleep() //终端
    tap(822, 670).sleep() //Resource Collection
    tap(842, 329).sleep()
    tap(830, 258).sleep() //PR-X-2
}

val PR_XXX_2 = OperateLevel("PR-X-2（先锋/辅助芯片）") {
    tap(970, 203).sleep() //终端
    tap(822, 670).sleep() //Resource Collection
    tap(842, 329).sleep()
    tap(830, 258).sleep() //PR-X-2
}

val PR_XXXX_2 = OperateLevel("PR-X-2（近卫/特种芯片）") {
    tap(970, 203).sleep() //终端
    tap(822, 670).sleep() //Resource Collection
    tap(842, 329).sleep()
    tap(830, 258).sleep() //PR-X-2
}
