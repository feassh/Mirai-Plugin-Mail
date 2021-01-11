package ceneax.other.miraipluginmail

import kotlinx.coroutines.CoroutineExceptionHandler
import net.mamoe.mirai.console.command.CommandSender.Companion.toCommandSender
import net.mamoe.mirai.console.extension.PluginComponentStorage
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.utils.error
import net.mamoe.mirai.utils.info

object PluginMain : KotlinPlugin(JvmPluginDescription(
        id = "ceneax.other.miraipluginmail",
        version = "0.0.1"
    )) {

    override fun PluginComponentStorage.onLoad() {
        logger.info { "'邮件服务器'插件加载成功！" }
    }

    override fun onEnable() {
        logger.info { "'邮件服务器'插件已启用！" }

        globalEventChannel().subscribeAlways(MessageEvent::class, CoroutineExceptionHandler { _, throwable ->
            logger.error(throwable)
        }) call@{
            // 判断插件是否已经启用
            if (!isEnabled) return@call

            bot.getFriendOrFail(1379719591).sendMessage("aaa")
        }
    }

    override fun onDisable() {
        logger.info { "'邮件服务器'插件已禁用！" }
    }

}