package com.moldavets.tiktok_telegram_bot.service.Impl;

import com.moldavets.tiktok_telegram_bot.logger.Impl.TelegramCustomLogger;
import com.moldavets.tiktok_telegram_bot.model.Impl.TelegramUser;
import com.moldavets.tiktok_telegram_bot.model.TelegramUserStatus;
import com.moldavets.tiktok_telegram_bot.repository.TelegramUserRepository;
import com.moldavets.tiktok_telegram_bot.service.TelegramUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Set;

@Service
public class TelegramUserServiceImpl implements TelegramUserService {

    private TelegramCustomLogger LOGGER;

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
    @Transactional(readOnly = true)
    public Set<TelegramUser> getAll() {
        return telegramUserRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Set<TelegramUser> getAllActiveTelegramUsers() {
        return telegramUserRepository.findAllByStatusIsNotIn(Set.of(
                TelegramUserStatus.KICKED.getStatusName(),
                TelegramUserStatus.LEFT.getStatusName(),
                TelegramUserStatus.RESTRICTED.getStatusName()
        ));
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
                            TelegramUserStatus.MEMBER.getStatusName(),
                            false
            ));
            TelegramCustomLogger.getInstance().info(
                    String.format("Registration for [%s|@%s] successful", userId, username)
            );
        } else if (!telegramUser.getStatus().equals(TelegramUserStatus.MEMBER.getStatusName())){
            telegramUserRepository.updateTelegramUserStatusById(userId, TelegramUserStatus.MEMBER.getStatusName());
            TelegramCustomLogger.getInstance().info(
                    String.format("Status for [%s|%s] has been changed to %s",
                    userId,
                    username,
                    TelegramUserStatus.MEMBER.getStatusName()
                )
            );
        }
    }

    @Override
    @Transactional
    public void save(TelegramUser telegramUser) {
        telegramUserRepository.save(telegramUser);
        TelegramCustomLogger.getInstance().info(String.format("Registration for [%s|%s] successful",
                telegramUser.getId(),
                telegramUser.getUsername()
        ));
    }

    @Override
    @Transactional
    public void updateStatusById(Long id, TelegramUserStatus telegramUserStatus) {
        telegramUserRepository.updateTelegramUserStatusById(id, telegramUserStatus.getStatusName());
        TelegramCustomLogger.getInstance().info(String.format("Status for [%s] has been changed to %s", id, telegramUserStatus.getStatusName()));
    }

    @Override
    @Transactional
    public void updateSubscriptionForAllUsers(boolean isSubscribed) {
        telegramUserRepository.updateAllTelegramUsersSubscription(isSubscribed);
        TelegramCustomLogger.getInstance().warn("Subscription for all users has been changed to " + isSubscribed);
    }

    @Override
    @Transactional
    public void updateSubscribeById(Long userId, Boolean isSubscribed) {
        telegramUserRepository.updateTelegramUserIsSubscribedById(userId, isSubscribed);
        TelegramCustomLogger.getInstance().info(String.format("Subscription for [%s] has been changed to - %s",
                userId,
                isSubscribed
        ));
    }

    @Override
    @Transactional
    public void updateLastModifiedDateById(Long id) {
        telegramUserRepository.updateTelegramUserLastModifiedDateById(id, Instant.now());
    }
}
