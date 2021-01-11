package ceneax.other.miraipluginmail

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.unregisterAll
import net.mamoe.mirai.console.command.CommandSender.Companion.toCommandSender
import net.mamoe.mirai.console.extension.PluginComponentStorage
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.utils.error
import net.mamoe.mirai.utils.info
import net.mamoe.mirai.utils.warning
import org.subethamail.smtp.helper.SimpleMessageListener
import org.subethamail.smtp.helper.SimpleMessageListenerAdapter
import org.subethamail.smtp.server.SMTPServer
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*

object PluginMain : KotlinPlugin(JvmPluginDescription(
        id = "ceneax.other.miraipluginmail",
        version = "0.0.1"
    )) {

    private lateinit var mailServer: SMTPServer

    override fun PluginComponentStorage.onLoad() {
        logger.info { "'邮件服务器'插件加载成功！" }
    }

    override fun onEnable() {
        logger.info { "'邮件服务器'插件已启用！" }

        // 监听QQ消息
        globalEventChannel().subscribeAlways(MessageEvent::class, CoroutineExceptionHandler { _, throwable ->
            logger.error(throwable)
        }) call@{
            // 判断插件是否已经启用
            if (!isEnabled) return@call

//            bot.getFriendOrFail(1379719591).sendMessage("aaa")
        }

        // 启动mail smtp服务
        mailServer = SMTPServer(SimpleMessageListenerAdapter(object : SimpleMessageListener {
            override fun accept(from: String?, recipient: String?): Boolean {
                logger.info { "收到新邮件，发信人: $from, 收信人: $recipient" }
                return true
            }

            override fun deliver(from: String?, recipient: String?, data: InputStream?) {
                // 输入流转字符串文本
                val contentRaw = BufferedReader(InputStreamReader(data)).useLines { lines ->
                    val sb = StringBuilder()
                    lines.forEach { sb.append(it) }
                    sb.toString()
                }

                logger.info { "邮件原始内容: $contentRaw" }

                // 发送给QQ
                val contents = contentRaw.split("Content-Transfer-Encoding: base64")
                if (contents.size == 2) {
                    logger.info { "邮件解析成功！" }

                    val content = String(Base64.getDecoder().decode(contents[1]))
                    launch {
                        sendMessage(content)

                        logger.info { "邮件转发成功！" }
                    }
                } else {
                    logger.warning { "邮件解析失败！" }
                }
            }
        }))
        mailServer.start()
    }

    private suspend fun sendMessage(msg: String) {
        Bot.instances.firstOrNull()?.getFriend(1379719591)?.sendMessage(msg)
    }

    override fun onDisable() {
        logger.info { "'邮件服务器'插件已禁用！" }
        mailServer.stop()
    }

}