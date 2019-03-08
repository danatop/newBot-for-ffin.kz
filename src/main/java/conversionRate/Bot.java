package conversionRate;


import conversionRate.model.ConversionRate;
import conversionRate.model.Currency;
import conversionRate.repository.ConversionRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Component
public class Bot extends TelegramLongPollingBot {

    private static DecimalFormat df = new DecimalFormat(".#");


    @Autowired
    private ConversionRateRepository conversionRateRepository;


    public void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try {
            setButtons(sendMessage);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            switch (message.getText()) {
                case "/help":
                    sendMsg(message, "Чем могу помочь?");
                    break;
                default:
                    try{
                        sendMsg(message, getText());
                    } catch (Exception e) {
                        sendMsg(message, "Error");
                    }
            }
        }

    }

    private String getText()
    {
        Optional<ConversionRate> conversionRate = conversionRateRepository.findByFromAndTo(Currency.USD, Currency.KZT);
        if (conversionRate.isPresent())
        {
            return "Курс на " + DateFormat.getDateInstance(SimpleDateFormat.LONG, new Locale("ru"))
                    .format(conversionRate.get().getRateDate()) + ":" + "\n" +
                    "Доллар США: " + df.format(conversionRate.get().getRate()) +" тенге";
        }
        return "";

    }
    public void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("Получить курс"));

        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);


    }

    public String getBotUsername() {
        return "KZTtoUSDBot";
    }

    public String getBotToken() {
        return "717074897:AAFD5woZK2SF9sR4OdntELTZe2e5byaBqeo";
    }
}
