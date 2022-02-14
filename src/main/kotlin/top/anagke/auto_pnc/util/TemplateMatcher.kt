package top.anagke.auto_pnc.util

import mu.KotlinLogging
import top.anagke.auto_android.device.Device
import top.anagke.auto_android.img.Img
import top.anagke.auto_android.img.Tmpl
import top.anagke.auto_pnc.AutoPnc

object TemplateMatcher {

    @JvmStatic
    fun main(args: Array<String>) {
        val cap = Device().cap()
        match(cap)
    }

    private val logger = KotlinLogging.logger { }

    fun match(img: Img) {
        ResourceScanner
            .scan(AutoPnc::class.java.`package`)
            .asSequence()
            .filter { it.endsWith(".png") }
            .map { tmplImgPath ->
                val tmplImgData = ClassLoader.getSystemResourceAsStream(tmplImgPath).use { it!!.readBytes() }
                val tmplImg = Img.decode(tmplImgData)!!
                val tmpl = Tmpl(tmplImgPath, threshold = 0.01, tmplImg)
                if (tmpl.img.size != img.size) return@map null
                tmpl
            }
            .filterNotNull()
            .map { it to it.diff(img) }
            .sortedBy { (_, diff) -> diff }
            .toList()
            .forEach { (tmplImg, diff) -> logger.info { "$img is ${diff.formatDiff()} different to $tmplImg" } }
    }

    private fun Double.formatDiff() = "%.6f".format(this)

}