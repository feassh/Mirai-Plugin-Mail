package ceneax.other.miraipluginmail.util

import org.jsoup.Jsoup
import java.io.IOException
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.internet.MimeMultipart

object MailUtil {

    @Throws(MessagingException::class, IOException::class)
    fun getTextFromMessage(message: Message): String {
        var result = ""
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString()
        } else if (message.isMimeType("multipart/*")) {
            val mimeMultipart = message.getContent() as MimeMultipart
            result = getTextFromMimeMultipart(mimeMultipart)
        }
        return result
    }

    @Throws(MessagingException::class, IOException::class)
    private fun getTextFromMimeMultipart(
        mimeMultipart: MimeMultipart
    ): String {
        var result = ""
        val count = mimeMultipart.count
        for (i in 0 until count) {
            val bodyPart = mimeMultipart.getBodyPart(i)
            if (bodyPart.isMimeType("text/plain")) {
                result = """
                $result
                ${bodyPart.content}
                """.trimIndent()
                break // without break same text appears twice in my tests
            } else if (bodyPart.isMimeType("text/html")) {
                val html = bodyPart.content as String
                result = """
                $result
                ${Jsoup.parse(html).text()}
                """.trimIndent()
            } else if (bodyPart.content is MimeMultipart) {
                result = result + getTextFromMimeMultipart(bodyPart.content as MimeMultipart)
            }
        }
        return result
    }

}