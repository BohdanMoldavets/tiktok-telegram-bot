package com.moldavets.tiktok_telegram_bot.service.Impl;

import com.moldavets.tiktok_telegram_bot.model.TelegramChannelStatus;
import com.moldavets.tiktok_telegram_bot.model.Impl.TelegramChannel;
import com.moldavets.tiktok_telegram_bot.repository.TelegramChannelRepository;
import com.moldavets.tiktok_telegram_bot.service.TelegramChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TelegramChannelServiceImpl implements TelegramChannelService {

    private final TelegramChannelRepository telegramChannelRepository;

    @Autowired
    public TelegramChannelServiceImpl(TelegramChannelRepository telegramChannelRepository) {
        this.telegramChannelRepository = telegramChannelRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public TelegramChannel getById(Long id) {
        return telegramChannelRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TelegramChannel> getAllWhereStatusIsActive() {
        return telegramChannelRepository.findAllByStatus(TelegramChannelStatus.ACTIVE);
    }

    @Override
    @Transactional
    public void save(TelegramChannel telegramChannel) {
        telegramChannelRepository.save(telegramChannel);
    }

    @Override
    @Transactional
    public void saveOrUpdate(TelegramChannel telegramChannel) {
        TelegramChannel storedChannel = telegramChannelRepository.findById(telegramChannel.getId()).orElse(null);
        if(storedChannel != null) {
            telegramChannelRepository.updateTelegramChannelById(telegramChannel.getId(), telegramChannel.getChannelLink(), TelegramChannelStatus.ACTIVE);
        } else {
            telegramChannelRepository.save(telegramChannel);
        }
    }

    @Override
    @Transactional
    public void updateStatusById(Long id, TelegramChannelStatus status) {
        telegramChannelRepository.updateTelegramChannelStatusById(id, status);
    }
}
