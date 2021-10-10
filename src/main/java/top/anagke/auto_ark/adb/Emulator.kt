package top.anagke.auto_ark.adb

import kotlinx.serialization.Serializable
import top.anagke.auto_ark.native.killProc
import top.anagke.auto_ark.native.openProc
import top.anagke.auto_ark.native.readText
import java.io.Closeable
import java.io.File


const val adbPath = "bin/adb/adb.exe"


@Serializable
sealed class Emulator : Closeable {

    abstract val adbHost: String
    abstract val adbPort: Int

    abstract fun open(): Device
    abstract fun isRunning(): Boolean

    fun connect(): Device {
        val adbAddress = "${adbHost}:${adbPort}"
        val regex = Regex("""$adbAddress\s*device""")
        while (regex !in adbProc("devices").readText().stdout) {
            adbProc("connect", adbAddress).readText()
        }

        val device = Device(adbAddress)
        while (true) {
            try {
                device.cap()
                break
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }
        }
        return device
    }

}

@Serializable
class Memu(
    private val location: String,
) : Emulator() {

    init {
        File("bin/adb/")
            .listFiles()
            ?.forEach { it.copyTo(File(location).resolveSibling(it.name), overwrite = true) }
    }


    override val adbHost: String = "127.0.0.1"

    override val adbPort: Int = 21503


    override fun open(): Device {
        val running = isRunning()
        if (running.not()) {
            startMemu()
        }
        return connect()
    }

    override fun isRunning(): Boolean {
        return "MEmu.exe" in openProc("tasklist", "/fi", "Imagename eq MEmu.exe").readText().stdout
    }

    override fun close() {
        stopMemu()
    }


    private fun startMemu() {
        openProc("${File(location).resolve("MEmu.exe")}", "MEmu")
    }

    private fun stopMemu() {
        killProc("adb.exe").readText()
        killProc("MEmu.exe").readText()
        killProc("MEmuHeadless.exe").readText()
    }

}

@Serializable
class BlueStacks(
    private val blueStacksHome: String = "C:/Program Files/BlueStacks_nxt",
) : Emulator() {

    override val adbHost: String = "localhost"
    override val adbPort: Int = 5555

    override fun open(): Device {
        if (isRunning().not()) {
            openProc(File(blueStacksHome).resolve("HD-Player.exe").canonicalPath, "--instance", "Nougat32")
        }
        return connect()
    }

    override fun isRunning(): Boolean {
        return "HD-Player.exe" in openProc("tasklist", "/fi", "Imagename eq HD-Player.exe").readText().stdout
    }

    override fun close() {
        killProc("HD-Player.exe")
    }

}