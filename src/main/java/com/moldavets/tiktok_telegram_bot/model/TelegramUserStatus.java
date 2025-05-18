package com.moldavets.tiktok_telegram_bot.model;

public enum TelegramUserStatus {
    ADMINISTRATOR("administrator"),
    KICKED("kicked"),
    LEFT("left"),
    MEMBER("member"),
    CREATOR("creator"),
    RESTRICTED("restricted");


    private final String statusName;

    TelegramUserStatus(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusName() {
        return statusName;
    }
}
