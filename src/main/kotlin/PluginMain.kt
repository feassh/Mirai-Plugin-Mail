package ceneax.other.miraipluginmail

import ceneax.other.miraipluginmail.util.MailUtil
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.extension.PluginComponentStorage
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.utils.info
import net.mamoe.mirai.utils.warning
import org.subethamail.smtp.helper.SimpleMessageListener
import org.subethamail.smtp.helper.SimpleMessageListenerAdapter
import org.subethamail.smtp.server.SMTPServer
import org.subethamail.wiser.Wiser
import org.subethamail.wiser.WiserMessage
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*
import javax.mail.Session
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

object PluginMain : KotlinPlugin(JvmPluginDescription(
        id = "ceneax.other.miraipluginmail",
        version = "0.0.1"
    )) {

    private lateinit var mailServer: Wiser
    private val mSdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

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
        mailServer = object : Wiser() {
            override fun accept(from: String?, recipient: String?): Boolean {
                logger.info { "收到新邮件，发信人: $from, 收信人: $recipient" }
                return super.accept(from, recipient)
            }

            override fun deliver(from: String?, recipient: String?, data: InputStream?) {
                super.deliver(from, recipient, data)

                val result = mailServer.messages.first().mimeMessage
                val sb = StringBuilder()
                MailUtil.getTextFromMessage(result).split("\n").forEach {
                    if (it.replace(" ", "").isNotEmpty()) {
                        sb.append("\n" + it.trim())
                    }
                }

                launch {
                    sendMessage(
                        "收到一封邮件！\n" +
                            "时间：${mSdf.format(result.sentDate)}\n" +
                            "来自：${result.from.first()}\n" +
                            "主题：${result.subject}\n" +
                            "内容：$sb"
                    )

                    mailServer.messages.clear()
                }
            }
        }
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