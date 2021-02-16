package grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.sql.Timestamp;
import java.util.List;

public class GrpcClient {
    public static void main(String[] args) {
        System.out.println();
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080).usePlaintext().build();

        MessageServiceGrpc.MessageServiceBlockingStub stub = MessageServiceGrpc.newBlockingStub(channel);

        System.out.println("/* --------------------------------------------------------------------------------------------- */");


        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String savedUniqueString = getUniqueString();

        stub.postMessage(Message.newBuilder()
                .setId(savedUniqueString)
                .setTimestamp(timestamp.getTime())
                .setSender("patek")
                .setTopic("info")
                .setContent("This is a message")
                .build());
        System.out.println("postMessage response: \n" + "Empty");


        System.out.println("/* --------------------------------------------------------------------------------------------- */");


        ListMessages listMessages = stub.listMessages(Topic.newBuilder().setTopic("info").build());
        List list = listMessages.getMessagesList();
        System.out.println("listMessages response: \n" + list);


        System.out.println("/* --------------------------------------------------------------------------------------------- */");


        Message message = stub.retrieveMessage(Id.newBuilder().setId(savedUniqueString).build());
        System.out.println("retrieveMessage response: \n" + message);


        System.out.println("/* --------------------------------------------------------------------------------------------- */");


        MapMessagesTimestamps map = stub.listMessagesWithTimestamps(Topic.newBuilder().setTopic("info").build());
        System.out.println("listMessageWithTimestamp response: \n" + map);


        System.out.println("/* --------------------------------------------------------------------------------------------- */");


        ListString listTopics = stub.listTopics(Empty.getDefaultInstance());
        System.out.println("listTopics response: \n" + listTopics);


        System.out.println("/* --------------------------------------------------------------------------------------------- */");


        Boolean bool = stub.subscribe(UserTopic.newBuilder()
                .setUser("patek")
                .setTopic("info")
                .build());
        System.out.println("subscribe response: \n" + bool.getBoolean());


        System.out.println("/* --------------------------------------------------------------------------------------------- */");


        ListString listSubscribers = stub.listSubscribers(Topic.newBuilder().setTopic("info").build());
        System.out.println("listSubscribers response: \n" + listSubscribers);


        System.out.println("/* --------------------------------------------------------------------------------------------- */");


        bool = stub.unsubscribe(UserTopic.newBuilder()
                .setUser("patek")
                .setTopic("info")
                .build());
        System.out.println("unsubscribe response: \n" + bool.getBoolean());


        System.out.println("/* --------------------------------------------------------------------------------------------- */");


        ListString listNodes = stub.listNodes(Empty.getDefaultInstance());
        System.out.println("listNodes response: \n" + listNodes);


        System.out.println("/* --------------------------------------------------------------------------------------------- */");


        channel.shutdown();
    }

    /**
     * Creates an unique string with Math.random
     *
     * @return a random string
     */
    private static String getUniqueString(){

        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(100);

        for (int i = 0; i < 100; i++) {
            int index = (int)(alphabet.length() * Math.random());
            sb.append(alphabet.charAt(index));
        }
        return sb.toString();
    }
}


