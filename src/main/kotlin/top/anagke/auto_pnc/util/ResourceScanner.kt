package top.anagke.auto_pnc.util

import org.reflections.Reflections
import org.reflections.scanners.Scanners

object ResourceScanner {

    fun scan(pkg: Package): List<String> {
        return Reflections(pkg.name, Scanners.Resources)
            .getResources(Regex(".*").toPattern())
            .toList()
    }

}