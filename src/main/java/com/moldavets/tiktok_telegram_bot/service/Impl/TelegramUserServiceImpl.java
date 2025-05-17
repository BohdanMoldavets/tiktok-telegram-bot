package com.moldavets.tiktok_telegram_bot.service.Impl;

import com.moldavets.tiktok_telegram_bot.model.ConditionStatus;
import com.moldavets.tiktok_telegram_bot.model.Impl.TelegramUser;
import com.moldavets.tiktok_telegram_bot.repository.TelegramUserRepository;
import com.moldavets.tiktok_telegram_bot.service.TelegramUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class TelegramUserServiceImpl implements TelegramUserService {

    private final TelegramUserRepository telegramUserRepository;

    @Autowired
    public TelegramUserServiceImpl(TelegramUserRepository telegramUserRepository) {
        this.telegramUserRepository = telegramUserRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public TelegramUser getById(Long id) {
        return telegramUserRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void checkTelegramUserRegistration(Long userId, String username) {
        TelegramUser telegramUser = telegramUserRepository.findById(userId).orElse(null);
        if(telegramUser == null) {
            telegramUserRepository.save(
                    new TelegramUser(
                            userId,
                            username,
                            ConditionStatus.ACTIVE,
                            false
                    )
            );
        } else {
            telegramUserRepository.updateTelegramUserLastModifiedDateById(userId, Instant.now());
        }
    }

    @Override
    @Transactional
    public void save(TelegramUser telegramUser) {
        telegramUserRepository.save(telegramUser);
    }

    @Override
    @Transactional
    public void updateSubscribeById(Long userId, Boolean isSubscribed) {
        telegramUserRepository.updateTelegramUserIsSubscribedById(userId, isSubscribed);
    }

    @Override
    @Transactional
    public void updateLastModifiedDateById(Long id) {
        telegramUserRepository.updateTelegramUserLastModifiedDateById(id, Instant.now());
    }
}
