package com.koflox.bot

import org.telegram.telegrambots.ApiContextInitializer
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.exceptions.TelegramApiException

object Main {

        @JvmStatic
        fun main(args: Array<String>) {
            ApiContextInitializer.init()
            try {
                TelegramBotsApi().registerBot(GentlemanClubBot())
            } catch (e: TelegramApiException) {
                e.printStackTrace()
            }
        }

}