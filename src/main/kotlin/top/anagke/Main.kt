package top.anagke

import top.anagke.auto_pnc.AutoPnc
import top.anagke.auto_pnc.AutoPncConfig

fun main() {
    val config = AutoPncConfig.loadConfig()
    config.emulator.launch().use { emu ->
        AutoPnc(config, emu.device).doRoutine()
    }
}