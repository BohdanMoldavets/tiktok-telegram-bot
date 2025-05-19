package com.moldavets.tiktok_telegram_bot.repository;

import com.moldavets.tiktok_telegram_bot.model.TelegramChannelStatus;
import com.moldavets.tiktok_telegram_bot.model.Impl.TelegramChannel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TelegramChannelRepository extends CrudRepository<TelegramChannel, Long> {
    List<TelegramChannel> findAllByStatus(TelegramChannelStatus status);

    @Modifying
    @Query("UPDATE TelegramChannel t SET t.status=?2 WHERE t.id=?1")
    void updateTelegramChannelStatusById(Long id, TelegramChannelStatus status);

    @Modifying
    @Query("UPDATE TelegramChannel t SET t.channelLink=?2, t.status=?3 WHERE t.id=?1")
    void updateTelegramChannelById(Long id, String link ,TelegramChannelStatus status);
}
