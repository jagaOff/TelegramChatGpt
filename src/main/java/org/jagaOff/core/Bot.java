package org.jagaOff.core;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import okhttp3.OkHttpClient;
import org.jagaOff.config.Config;

public class Bot {
    private TelegramBot bot;
    private Update update;

    public Bot() {
        OkHttpClient client = new OkHttpClient();
        bot = new TelegramBot.Builder(Config.TOKEN).okHttpClient(client).build();
        // Create Exception Handler
        bot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                this.update = update;
                Message message = update.message();
                messageHandler(message);
            }

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void messageHandler(Message message) {
        switch (message.text()) {
            case "/start" -> sendMessage(Config.HELLO_MESSAGE);
            case "/help" -> sendReplyMessage(Config.HELP_MESSAGE);
            case "text" -> sendReplyMessage("🔗 **");
            default -> sendReplyMessage("I dont know it =)");
        }
    }

    public void sendMessage(String message) {
        SendResponse response = bot.execute(new SendMessage(update.message().chat().id(), message)
                .parseMode(ParseMode.Markdown));
    }

    public void sendReplyMessage(String message) {
        SendResponse response = bot.execute(new SendMessage(update.message().chat().id(), message)
                .parseMode(ParseMode.Markdown)
                .replyToMessageId(update.message().messageId()));

        if(!response.isOk()) System.out.println("response not send");
    }

}
