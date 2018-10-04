package com.rob78596.regionpinger;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.joda.time.Instant;

import java.io.IOException;
import java.net.InetAddress;

public class Main implements RequestHandler<Parameters, String> {

    public String handleRequest(Parameters input, Context context) {
        System.out.println("Running regionpinger lambda");
        System.out.println("Input object" + input);

        System.out.println("Attempting to ping: "+input.getDestination());

        try {
            //do a DNS lookup?
            InetAddress destIp = InetAddress.getByName(input.getDestination());

            //warmup ping, ensure DNS lookup cached
            destIp.isReachable(5000); //ping it!

            //the real test
            long start = System.nanoTime();
            destIp.isReachable(5000); //ping it!
            long nanos = System.nanoTime() - start;

            String micros = Double.toString(nanos / 1e3);
            String millis = Double.toString(nanos / 1e6);

            System.out.println("Nanos: " + nanos);
            System.out.println("Micros: " + micros);
            System.out.println("Millis: " + millis);


            AmazonDynamoDB db = AmazonDynamoDBClientBuilder.standard()
                    .withRegion("eu-west-2").build();
            DynamoDB db2 = new DynamoDB(db);
            Table table = db2.getTable("RegionPinger");

            Item item = new Item();
            item.withString("Date", Instant.now().toString());
            item.withString("Source", input.getSource());
            item.withString("Destination", input.getDestination());
            item.withString("Latency", millis);
            table.putItem(item);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "returning some text from the lambda";
    }
}
