package com.koflox.bot

import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GentlemanClubBot : TelegramLongPollingBot() {

    private val timurJoke1 = "Мама собирает сыну обед в школу:\n" +
            "— Вот, положила тебе в ранец хлеб, колбасу и гвозди.\n" +
            "— Мам, нафига??\n" +
            "— Ну как же, берешь хлеб, кладешь на него колбасу и ешь.\n" +
            "— А гвозди?\n" +
            "— Так вот же они!"
    private val timurJoke2 = "— Для чего вам зонт?\n" +
            "— А вдруг дождь.\n" +
            "— Я впервые вижу человека, который боится дождя в помещении.\n" +
            "— А я и не боюсь. У меня ведь зонт."
    private val timurJoke3 = "Купил мужик шляпу, а она ему как раз... два... три"
    private val timurPhrase1 = "Идите работайте, тунеядцы! Постят срань всякую"
    private val listOfTimurPhrasis = listOf(timurJoke1, timurJoke2, timurJoke3)

    private val retrofit: Retrofit by lazy { getRetrofitClient() }
    private val imgurApiService: ImgurApiService by lazy {
        retrofit.create(ImgurApiService::class.java)
    }

    override fun getBotUsername() = "com.koflox.bot.GentlemanClubBot"

    override fun getBotToken() = "827293121:AAHeDjjc-Q9_U6k5_Q3ag5ca2EX4tLnxGIk"

    override fun onUpdateReceived(update: Update?) {
        val message = update?.message
        message?.text?.run {
            when {
                startsWith("/get ") -> sendRandomSubredditPhoto(message)
                toLowerCase() == "/nikita" -> sendTextMessage(message, "Йоу, собаки, я Никита-Узумаки!")
                toLowerCase() == "/mrfanq" -> sendTextMessage(message, "I am a robot!")
                toLowerCase() == "/artem" -> sendTextMessage(message, "Хихихе хихе")
                toLowerCase() == "/timur" -> sendTextMessage(message, timurPhrase1)
                toLowerCase() == "/timurjokes" -> sendTextMessage(message, listOfTimurPhrasis.random())
                else -> sendInvalidRequest(message)
            }
        }
    }

    private fun sendTextMessage(message: Message, textMsg: String) {
        val messageToSend = SendMessage().apply {
            enableMarkdown(true)
            chatId = message.chatId.toString()
            replyToMessageId = message.messageId
            text = textMsg
        }
        try {
            execute(messageToSend)
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }

    private fun sendInvalidRequest(message: Message) {
        val messageToSend = SendMessage().apply {
            enableMarkdown(true)
            chatId = message.chatId.toString()
            replyToMessageId = message.messageId
            text = "Invalid request, хули ты тут вводишь пидор?!"
        }
        try {
            execute(messageToSend)
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }

    private fun sendRandomSubredditPhoto(message: Message) {
//        val firstPhotoLink = imgurApiService.getSubredditPhotos("boobs").execute().raw().toString()
        val subredditName = message.text.replace("/get ", "")
            .replace(" ", "_")
        println("subredditName: $subredditName")
        val allPhotos = imgurApiService.getSubredditPhotos(subredditName).execute().body()?.data ?: listOf()

        val randomPhotoLink = if (allPhotos.isNotEmpty()) allPhotos.random().link else null

        randomPhotoLink?.let { photoLink ->
            println(randomPhotoLink)

            val messageToSend = SendMessage().apply {
                enableMarkdown(true)
                chatId = message.chatId.toString()
                text = photoLink
            }
            try {
                execute(messageToSend)
            } catch (e: TelegramApiException) {
                e.printStackTrace()
            }
        } ?: kotlin.run {
            sendTextMessage(message, "nothing has been found")
            println("nothing has been found")
//            sendInvalidRequest(message)
        }
    }

}

fun getRetrofitClient(): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://api.imgur.com/3/")
        .addConverterFactory(GsonConverterFactory.create())
//        .addConverterFactory(MoshiConverterFactory.create())
        .build()
}