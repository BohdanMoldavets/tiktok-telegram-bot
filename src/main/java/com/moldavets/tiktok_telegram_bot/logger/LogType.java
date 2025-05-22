package com.moldavets.tiktok_telegram_bot.logger;

public enum LogType {
    INFO(" [INFO] "),
    WARN(" [WARN] "),
    ERROR( " [ERROR] ");

    private final String logMark;

    LogType(String logMark) {
        this.logMark = logMark;
    }

    public String getLogMark() {
        return logMark;
    }
}
