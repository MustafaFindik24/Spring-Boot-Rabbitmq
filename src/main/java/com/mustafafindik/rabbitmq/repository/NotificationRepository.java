package com.mustafafindik.rabbitmq.repository;

import com.mustafafindik.rabbitmq.model.Notification;
import org.aspectj.weaver.ast.Not;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
}
