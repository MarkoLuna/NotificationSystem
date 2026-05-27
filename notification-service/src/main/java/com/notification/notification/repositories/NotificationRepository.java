package com.notification.notification.repositories;

import com.notification.api.model.NotificationCategory;
import com.notification.api.model.NotificationChannel;
import com.notification.notification.entities.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String> {

  Page<Notification> findByCreator(String creator, Pageable pageable);

  Page<Notification> findByChannelAndCategory(NotificationChannel channel, NotificationCategory category, Pageable pageable);
}
