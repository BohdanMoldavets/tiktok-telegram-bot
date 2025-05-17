package com.moldavets.tiktok_telegram_bot.repository;

import com.moldavets.tiktok_telegram_bot.model.ConditionStatus;
import com.moldavets.tiktok_telegram_bot.model.Impl.TelegramChannel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TelegramChannelRepository extends CrudRepository<TelegramChannel, Long> {
    List<TelegramChannel> findAllByStatus(ConditionStatus status);
}
