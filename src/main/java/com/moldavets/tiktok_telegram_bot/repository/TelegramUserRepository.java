package com.moldavets.tiktok_telegram_bot.repository;

import com.moldavets.tiktok_telegram_bot.model.Impl.TelegramUser;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Set;

@Repository
public interface TelegramUserRepository extends CrudRepository<TelegramUser, Long> {

    Set<TelegramUser> findAll();

    Set<TelegramUser> findAllByStatusIsNotIn(Set<String> statuses);

    @Modifying
    @Query("UPDATE TelegramUser t SET t.lastModifiedDate = ?2 WHERE t.id = ?1")
    void updateTelegramUserLastModifiedDateById(Long id , Instant lastModifiedDate);

    @Modifying
    @Query("UPDATE TelegramUser t SET t.isSubscribed=?2 WHERE t.id=?1")
    void updateTelegramUserIsSubscribedById(Long id , boolean subscribed);

    @Modifying
    @Query("UPDATE TelegramUser t SET t.status=?2 WHERE t.id=?1")
    void updateTelegramUserStatusById(Long id , String status);

    @Modifying
    @Query("UPDATE TelegramUser t SET t.isSubscribed=?1")
    void updateAllTelegramUsersSubscription(boolean isSubscribed);

    @Modifying
    @Query("UPDATE TelegramUser t SET t.isBanned = ?2 WHERE t.id = ?1")
    void updateTelegramUserIsBannedById(Long id, Boolean isBanned);

}
