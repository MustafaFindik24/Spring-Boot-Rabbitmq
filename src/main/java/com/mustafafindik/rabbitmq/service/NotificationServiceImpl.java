package com.mustafafindik.rabbitmq.service;

import com.mustafafindik.rabbitmq.model.Notification;
import com.mustafafindik.rabbitmq.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService{

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }
    @Override
    public void saveNotify(Notification notification) {
        Notification saveNotify = new Notification();
        //saveNotify.setId(saveNotify.getId());
        saveNotify.setSeen(notification.getSeen());
        saveNotify.setCreatedAt(notification.getCreatedAt());
        saveNotify.setMessage(notification.getMessage());
        notificationRepository.save(saveNotify);
        log.info("Notification saved to the database." + saveNotify);
    }
}
