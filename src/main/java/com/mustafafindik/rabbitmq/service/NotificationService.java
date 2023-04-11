package com.mustafafindik.rabbitmq.service;

import com.mustafafindik.rabbitmq.model.Notification;

public interface NotificationService {
    void saveNotify(Notification notification);
}
