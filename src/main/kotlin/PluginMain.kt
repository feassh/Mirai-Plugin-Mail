package ceneax.pther.miraipluginmail

import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.info

object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "ceneax.pther.miraipluginmail",
        version = "0.0.1"
    )
) {
    override fun onEnable() {
        logger.info { "Plugin loaded" }
    }
}