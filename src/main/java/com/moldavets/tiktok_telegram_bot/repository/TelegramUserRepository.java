package com.moldavets.tiktok_telegram_bot.repository;

import com.moldavets.tiktok_telegram_bot.model.Impl.TelegramUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramUserRepository extends CrudRepository<TelegramUser, Long> {

}
