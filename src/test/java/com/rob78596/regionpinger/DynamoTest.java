package com.rob78596.regionpinger;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import org.joda.time.Instant;
import org.junit.Test;

public class DynamoTest {

    @Test
    public void test() {
        AmazonDynamoDB db = AmazonDynamoDBClientBuilder.standard()
                .withRegion("eu-west-2").build();
        DynamoDB db2 = new DynamoDB(db);
        Table table = db2.getTable("RegionPinger");

        Item item = new Item();
        item.withString("Date", Instant.now().toString());
        item.withString("SecondField", "RobsValue");

        table.putItem(item);
    }
}
