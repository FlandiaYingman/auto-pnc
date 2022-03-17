package top.anagke.auto_pnc

import top.anagke.auto_android.AutoAndroid
import top.anagke.auto_android.AutoModule
import top.anagke.auto_android.device.*
import top.anagke.auto_android.img.Img
import top.anagke.auto_android.img.Tmpl
import top.anagke.auto_pnc.explore.ExploreModule
import top.anagke.auto_pnc.factory.FactoryModule
import kotlin.reflect.KProperty

fun tmpl(diff: Double = 0.01) = TmplDelegate(diff)

class TmplDelegate(private val diff: Double) {
    private var tmpl: Tmpl? = null
    operator fun getValue(thisRef: Any?, property: KProperty<*>): Tmpl {
        if (tmpl == null) {
            val name = "${property.name}.png"
            val kClass = thisRef?.let { it::class.java } ?: AutoPnc::class.java
            val tmplBytes = kClass.getResource(name)!!.readBytes()
            tmpl = Tmpl(name, diff, Img.decode(tmplBytes)!!)
        }
        return tmpl!!
    }
}

val PNC_ACTIVITY = AndroidActivity(
    "com.sunborn.neuralcloud.cn",
    "com.mica.micasdk.ui.FoundationActivity",
)

// 主界面
val 主界面: Tmpl by tmpl()

// 主界面
val 主界面_拖出: Tmpl by tmpl()

// 可跳回主界面
val 可跳出: Tmpl by tmpl()

fun Device.jumpOut() {
    assert(可跳出)
    tap(250, 50)
    await(主界面, 主界面_拖出)
    if (matched(主界面_拖出)) {
        dragv(840, 360, 400, 0).sleep() //使“心智检索”可见
    }
    await(主界面)
}

fun Device.jumpBack() {
    assert(可跳出)
    tap(120, 50).sleep()
}


class AutoPnc(
    val config: AutoPncConfig,
    device: Device,
) : AutoAndroid<AutoPnc>(device) {

    companion object {
        fun default(): AutoPnc {
            val config = AutoPncConfig.loadConfig()
            return AutoPnc(config, BlueStacks(config.emulator).launch().device)
        }
    }

    override val name = "自动云图"
    override val initModules = listOf<AutoModule<AutoPnc>>(
        LoginModule(this),
    )
    override val workModules = listOf<AutoModule<AutoPnc>>(
        DormModule(this),
        OasisModule(this),
        FactoryModule(this),
        SearchModule(this),
        ExploreModule(this),
        MissionModule(this),
        ExploreModule(this),
    )
    override val finalModules = listOf(
        createModule("清理模块") { device.stop(PNC_ACTIVITY, description = "停止云图计划") },
    )

    override fun isAtMain() = device.match(主界面)

    override fun returnToMain() = device.jumpOut()

}
