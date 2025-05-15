package com.moldavets.tiktok_telegram_bot.service.Impl;

import com.moldavets.tiktok_telegram_bot.service.SubscribeCheckerService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;

@Service
public class SubscribeCheckerServiceImpl implements SubscribeCheckerService {

    Long channelId = -1002422084878L;

    @Override
    public boolean isSubscribed(String chatId) {
//        GetChatMember getChatMember = new GetChatMember();
//        getChatMember.setChatId(channelId);
//        getChatMember.setUserId(Long.parseLong(chatId));

//        getChatMember.deserializeResponse()
    return false;
    }

}
