package com.moldavets.tiktok_telegram_bot.service.Impl;

import com.moldavets.tiktok_telegram_bot.model.Impl.TelegramUser;
import com.moldavets.tiktok_telegram_bot.repository.TelegramUserRepository;
import com.moldavets.tiktok_telegram_bot.service.TelegramUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramUserServiceImpl implements TelegramUserService {

    private final TelegramUserRepository telegramUserRepository;

    @Override
    public TelegramUser getById(Long id) {
        return telegramUserRepository.findById(id).orElse(null);
    }
}
