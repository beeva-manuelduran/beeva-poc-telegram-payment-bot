package com.beeva.poc.paytelegram;

import org.telegram.telegrambots.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.api.methods.AnswerShippingQuery;
import org.telegram.telegrambots.api.methods.send.SendInvoice;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.payments.LabeledPrice;
import org.telegram.telegrambots.api.objects.payments.ShippingOption;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class MyPaymentBot extends TelegramLongPollingBot {

    private final static String BOT_TOKEN = "1234567890:AAAABBBBCCCCDDDDeeeeFFFFgggGHHHH";
    private final static String PROVIDER_TOKEN = "1234567890:TEST:AAAABBBBCCCCDDDD";

    public void onUpdateReceived(Update update) {

        System.out.println("Message received: " + update);

        if (update.hasMessage() && update.getMessage().hasText()) {

            if (!update.getMessage().hasSuccessfulPayment()) {

                sendInvoice(update);

            }

        } else if (update.hasShippingQuery()) {

            selectDeliveryOptions(update);

        } else if (update.hasPreCheckoutQuery()) {

            sendPreChekout(update);
        }
    }


    public String getBotUsername() {
        return "mypaymentbot";
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }


    private void sendInvoice(Update update) {
        List<LabeledPrice> labeledPriceList = new ArrayList<LabeledPrice>();
        labeledPriceList.add(new LabeledPrice("Clean Code", 2610));
        labeledPriceList.add(new LabeledPrice("My Tax ;)", 500));

        SendInvoice sendInvoiceObject = new SendInvoice(
                Integer.parseInt(update.getMessage().getChatId().toString()),
                "Clean Code",
                "Even bad code can function. But if code isn't clean, it can bring a development organization" +
                        " to its knees. Every year, countless hours and significant resources are lost " +
                        "because of poorly written code.",
                "Happy Discount Beeva",
                PROVIDER_TOKEN,
                "clean-code-example",
                "EUR",
                labeledPriceList);
        sendInvoiceObject.setPhotoUrl("http://3.bp.blogspot" +
                ".com/_WQMxntNMWT0/TSRbBBi43LI/AAAAAAAALCw/Ub0vyDHg54Y/s1600/CleanCode.jpg");
        sendInvoiceObject.setPhotoHeight(512);
        sendInvoiceObject.setPhotoSize(512);
        sendInvoiceObject.setPhotoWidth(512);
        sendInvoiceObject.setNeedName(true);
        sendInvoiceObject.setNeedPhoneNumber(true);
        sendInvoiceObject.setNeedEmail(true);
        sendInvoiceObject.setNeedShippingAddress(true);
        sendInvoiceObject.setFlexible(true);

        try {
            sendInvoice(sendInvoiceObject);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void selectDeliveryOptions(Update update) {
        AnswerShippingQuery answerShippingQuery = new AnswerShippingQuery(
                update.getShippingQuery().getId(),
                true
        );

        List<ShippingOption> shippingOptionList = new ArrayList<ShippingOption>();

        List<LabeledPrice> labeledPricesCorreos = new ArrayList<LabeledPrice>();
        labeledPricesCorreos.add(new LabeledPrice("Express", 1500));
        List<LabeledPrice> labeledPricesSeur = new ArrayList<LabeledPrice>();
        labeledPricesSeur.add(new LabeledPrice("Express", 2200));

        shippingOptionList.add(new ShippingOption("correos", "Correos", labeledPricesCorreos));
        shippingOptionList.add(new ShippingOption("seur", "Seur", labeledPricesSeur));

        answerShippingQuery.setShippingOptions(shippingOptionList);

        try {
            answerShippingQuery(answerShippingQuery);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendPreChekout(Update update) {
        AnswerPreCheckoutQuery preCheckoutQuery = new AnswerPreCheckoutQuery(
                update.getPreCheckoutQuery().getId(),
                true
        );

        try {
            answerPreCheckoutQuery(preCheckoutQuery);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
