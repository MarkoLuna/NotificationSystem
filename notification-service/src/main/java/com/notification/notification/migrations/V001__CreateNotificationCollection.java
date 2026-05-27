package com.notification.notification.migrations;

import com.mongodb.client.MongoDatabase;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import lombok.extern.log4j.Log4j2;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Creates the notifications collection in MongoDB with validation rules.
 */
@Log4j2
@ChangeUnit(id = "create-notification-collection", order = "001", author = "system")
public class V001__CreateNotificationCollection {

    @Execution
    public void execution(MongoTemplate mongoTemplate) {
        // Create the notifications collection if it doesn't exist
        MongoDatabase database = mongoTemplate.getDb();
        
        if (!database.listCollectionNames().into(new java.util.ArrayList<>()).contains("notifications")) {
            database.createCollection("notifications");
        }
        
        // Add validation rules for the notifications collection
        Document validator = new Document()
                .append("$jsonSchema", new Document()
                        .append("bsonType", "object")
                        .append("required", java.util.List.of("creator", "message", "channel", "category", "createdAt"))
                        .append("properties", new Document()
                                .append("creator", new Document()
                                        .append("bsonType", "string")
                                        .append("description", "Username of the notification creator"))
                                .append("message", new Document()
                                        .append("bsonType", "string")
                                        .append("description", "Notification message content"))
                                .append("channel", new Document()
                                        .append("bsonType", "string")
                                        .append("enum", java.util.List.of("EMAIL", "SMS", "PUSH"))
                                        .append("description", "Notification channel"))
                                .append("category", new Document()
                                        .append("bsonType", "string")
                                        .append("enum", java.util.List.of("SPORTS", "MOVIES", "FINANCE", "TECHNOLOGY", "HEALTH"))
                                        .append("description", "Notification category"))
                                .append("createdAt", new Document()
                                        .append("bsonType", "date")
                                        .append("description", "Timestamp when notification was created"))));
        
        database.runCommand(new Document("collMod", "notifications")
                .append("validator", validator)
                .append("validationLevel", "moderate"));
    }

    @RollbackExecution
    public void rollbackExecution(MongoTemplate mongoTemplate) {
        log.error("Dropping notifications collection during rollback");
        MongoDatabase database = mongoTemplate.getDb();
        database.getCollection("notifications").drop();
    }
}
