package com.notification.notification.migrations;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import lombok.extern.log4j.Log4j2;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Creates indexes on the notifications collection for faster queries.
 */
@Log4j2
@ChangeUnit(id = "create-notification-indexes", order = "002", author = "system")
public class V002__CreateNotificationIndexes {

    @Execution
    public void execution(MongoTemplate mongoTemplate) {
        MongoDatabase database = mongoTemplate.getDb();
        MongoCollection<Document> collection = database.getCollection("notifications");
        
        // Create index on creator field for faster queries by creator
        collection.createIndex(Indexes.ascending("creator"));
        
        // Create compound index on channel and category for faster filtered queries
        collection.createIndex(Indexes.compoundIndex(
                Indexes.ascending("channel"),
                Indexes.ascending("category")
        ));
        
        // Create index on createdAt for time-based queries and sorting
        collection.createIndex(Indexes.descending("createdAt"));
        
        // Create compound index on creator and createdAt for user-specific time-based queries
        collection.createIndex(Indexes.compoundIndex(
                Indexes.ascending("creator"),
                Indexes.descending("createdAt")
        ));
    }

    @RollbackExecution
    public void rollbackExecution(MongoTemplate mongoTemplate) {

        log.error("Dropping notification indexes during rollback");
        MongoDatabase database = mongoTemplate.getDb();
        MongoCollection<Document> collection = database.getCollection("notifications");
        
        // Drop all indexes except the default _id index
        for (Document index : collection.listIndexes()) {
            String indexName = index.getString("name");
            if (!"_id_".equals(indexName)) {
                collection.dropIndex(indexName);
            }
        }
    }
}
