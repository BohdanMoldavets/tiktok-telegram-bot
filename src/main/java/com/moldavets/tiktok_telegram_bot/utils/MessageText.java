package com.moldavets.tiktok_telegram_bot.utils;

public enum MessageText {
//  ===RU===
    COMMAND_START("Привет, этот бот может скачивать видео без водяного знака с <b>TikTok</b> и <b>YouTube</b>. Для начала, отправь ссылку на видео"),
    COMMAND_ADMINISTRATOR_ACTIVE_USERS_COUNT("Количество активных пользователей"),
    COMMAND_ADMINISTRATOR_ALL_USERS_COUNT("Количество всех пользователей"),
    COMMAND_ADMINISTRATOR_CHANNEL_ADDED("Добавлен новый канал с id"),
    COMMAND_ADMINISTRATOR_CHANNEL_DELETED("Удален канал с id"),
    COMMAND_UNKNOWN("Команда не найдена❌"),
    CALLBACK_SUCCESSFUL_SUBSCRIPTION("\uD83C\uDD97 Вы подписались на каналы, теперь можете скачивать видео. Пожалуйста, пришлите ссылку на видео с TikTok."),
    CALLBACK_FAILED_SUBSCRIPTION("<b>Вы ещё не подписались на необходимые каналы.</b>\nПодпишитесь и попробуйте ещё раз"),
    DOWNLOADER_ASSIGN_CAPTION("\uD83D\uDCE5 @TikTokSaveAiBot"),
    DOWNLOADER_NEXT_VIDEO_REQUEST("Для того что бы скачать ещё, просто отправьте ссылку в бота"),
    DOWNLOADER_FAIL_WHILE_DOWNLOADING("Неверная ссылка, пожалуйста, отправьте правильную ссылку. Пример:\nhttps://vm.tiktok.com/kL901N/"),
    DOWNLOADER_SUBSCRIPTION_REQUEST("<b>Пожалуйста, подпишись на наш канал, мы для вас стараемся.</b> \n\n Поддержите подпиской на канал и затем нажмите кнопку «Проверить подписку \uD83D\uDD04» чтобы получить видео."),
    DOWNLOADER_UNKNOWN("Неверная ссылка, пожалуйста, отправьте правильную ссылку. Пример:\nhttps://vm.tiktok.com/kL901N/"),
    DOWNLOADER_SOMETHING_WENT_WRONG("<b>Что-то пошло не так!</b>\n\nНа данный момент бот не поддерживает слишком длинные видео\uD83D\uDE1E"), //when request video is too long
    KEYBOARD_BUTTON_SUBSCRIPTION_REQUEST("Подписаться на канал ✅"),
    KEYBOARD_BUTTON_CHECK_SUBSCRIPTION("Проверить подписку \uD83D\uDD04"),
    ADS_AMOUNT_OF_USERS_WHO_RECEIVED_AD("Количество людей которые получили рекламный пост"),
    EXCEPTION_OCCURRED("<b>Что-то пошло не так!</b>\n\nПопробуйте позже или обратитесь к разработчику!");

//    ===ENG===
//    COMMAND_START("Hello!!! Please send your tiktok link to download"),
//    COMMAND_ADMINISTRATOR_ACTIVE_USERS_COUNT("Count of active bot users"),
//    COMMAND_ADMINISTRATOR_ALL_USERS_COUNT("Count of all bot users"),
//    COMMAND_ADMINISTRATOR_CHANNEL_ADDED("Added new channel with id"),
//    COMMAND_ADMINISTRATOR_CHANNEL_DELETED("Deleted channel with id"),
//    COMMAND_UNKNOWN("Command not found!"),
//    CALLBACK_SUCCESSFUL_SUBSCRIPTION("Now you can download tiktok"),
//    CALLBACK_FAILED_SUBSCRIPTION("Subscription Failed"),
//    DOWNLOADER_ASSIGN_CAPTION("Download more tiktoks here TikTok @tiktok"),
//    DOWNLOADER_NEXT_VIDEO_REQUEST("Send another link for download"),
//    DOWNLOADER_FAIL_WHILE_DOWNLOADING("Looks like you enter wrong link!"),
//    DOWNLOADER_SUBSCRIPTION_REQUEST("Please subscribe on this groups for Download TikTok"),
//    DOWNLOADER_UNKNOWN("Downloader not found!"),
//    DOWNLOADER_SOMETHING_WENT_WRONG("Something went wrong!"), //when request video is too long
//    KEYBOARD_BUTTON_SUBSCRIPTION_REQUEST("Subscribe to this channel ✅"),
//    KEYBOARD_BUTTON_CHECK_SUBSCRIPTION("Check subscription \uD83D\uDD04"),
//    ADS_AMOUNT_OF_USERS_WHO_RECEIVED_AD("Amount of users who received advert"),
//    EXCEPTION_OCCURRED("Something went wrong, try again later, or contact to developer!");

    private final String messageText;

    MessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageText() {
        return messageText;
    }
}
