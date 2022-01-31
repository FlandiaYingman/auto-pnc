package top.anagke.auto_pnc

import top.anagke.auto_android.AutoModule

abstract class PncModule(auto: AutoPnc) : AutoModule<AutoPnc>(auto) {
    protected val config = auto.config
}