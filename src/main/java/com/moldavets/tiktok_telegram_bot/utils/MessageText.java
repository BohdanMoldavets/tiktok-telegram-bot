package com.moldavets.tiktok_telegram_bot.utils;

public enum MessageText {
    COMMAND_START("Hello!!! Please send your tiktok link to download"),
    COMMAND_ADMINISTRATOR_USERS_COUNT("Count of bot users"),
    COMMAND_ADMINISTRATOR_CHANNEL_ADDED("Added new channel with id"),
    COMMAND_ADMINISTRATOR_CHANNEL_DELETED("Deleted channel with id"),
    COMMAND_UNKNOWN("Command not found!"),
    CALLBACK_SUCCESSFUL_SUBSCRIPTION("Now you can download tiktok"),
    CALLBACK_FAILED_SUBSCRIPTION("Subscription Failed"),
    DOWNLOADER_ASSIGN_CAPTION("Download more tiktoks here TikTok @tiktok"),
    DOWNLOADER_NEXT_VIDEO_REQUEST("Send another link for download"),
    DOWNLOADER_FAIL_WHILE_DOWNLOADING("Looks like you enter wrong link!"),
    DOWNLOADER_SUBSCRIPTION_REQUEST("Please subscribe on this groups for Download TikTok"),
    DOWNLOADER_UNKNOWN("Downloader not found!"),
    DOWNLOADER_SOMETHING_WENT_WRONG("Something went wrong!"), //when request video is too long
    KEYBOARD_BUTTON_SUBSCRIPTION_REQUEST("Subscribe to this channel ✅"),
    KEYBOARD_BUTTON_CHECK_SUBSCRIPTION("Check subscription \uD83D\uDD04"),
    ADS_AMOUNT_OF_USERS_WHO_RECEIVED_AD("Amount of users who received advert"),
    EXCEPTION_OCCURRED("Something went wrong, try again later, or contact to developer!");

    private final String messageText;

    MessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageText() {
        return messageText;
    }
}
