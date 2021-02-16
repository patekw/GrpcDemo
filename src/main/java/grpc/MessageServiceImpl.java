package grpc;

import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MessageServiceImpl extends MessageServiceGrpc.MessageServiceImplBase {

    private ArrayList<Message> messages = new ArrayList<>();
    private Map<String, List<String>> subscribers = new HashMap<>();//<Topic, List<Users>>
    private ArrayList<String> nodes = new ArrayList<>();

    @Override
    public void postMessage(Message message, StreamObserver<Empty> responseObserver) {
        System.out.println("postMessage");
        messages.add(message);
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void listMessages(Topic topic, StreamObserver<ListMessages> responseObserver){
        System.out.println("listMessages");
        List<Message> list = new ArrayList<>();
        for (int i = 0; i < messages.size(); i++){
            if(messages.get(i).getTopic().equals(topic.getTopic())) {
                list.add(messages.get(i));
            }
        }
        responseObserver.onNext(ListMessages.newBuilder().addAllMessages(list).build());
        responseObserver.onCompleted();
    }

    @Override
    public void retrieveMessage(Id id, StreamObserver<Message> responseObserver){
        System.out.println("retrieveMessages");
        for (int i = 0; i < messages.size(); i++){
            if(messages.get(i).getId().equals(id.getId())) {
                responseObserver.onNext(messages.get(i));
                responseObserver.onCompleted();
                return;
            }
        }
        responseObserver.onNext(Message.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void listMessagesWithTimestamps(Topic topic, StreamObserver<MapMessagesTimestamps> responseObserver) {
        System.out.println("listMessagesWithTimestamps");
        Map map = messages.stream().collect(Collectors.toMap(Message::getId, Message::getTimestamp));
        responseObserver.onNext(MapMessagesTimestamps.newBuilder().putAllPair(map).build());
        responseObserver.onCompleted();
    }

    @Override
    public void listTopics(Empty empty, StreamObserver<ListString> responseObserver) {
        System.out.println("listTopics");
        List<String> list = new ArrayList<>();
        for (int i = 0; i < messages.size(); i++){
            list.add(messages.get(i).getTopic());
        }
        responseObserver.onNext(ListString.newBuilder().addAllString(list).build());
        responseObserver.onCompleted();
    }

    @Override
    public void subscribe(UserTopic userTopic, StreamObserver<Boolean> responseObserver){
        System.out.println("subscribe");
        if(!subscribers.containsKey(userTopic.getTopic())) {
            subscribers.put(userTopic.getTopic(), new ArrayList<>());
        }
        subscribers.get(userTopic.getTopic()).add(userTopic.getUser());
        if(subscribers.get(userTopic.getTopic()).contains(userTopic.getUser())) {
            responseObserver.onNext(Boolean.newBuilder().setBoolean(true).build());
            responseObserver.onCompleted();
        } else {
            responseObserver.onNext(Boolean.newBuilder().setBoolean(false).build());
            responseObserver.onCompleted();
        }
    }

    @Override
    public void unsubscribe(UserTopic userTopic, StreamObserver<Boolean> responseObserver){
        System.out.println("unsubscribe");
        if(!subscribers.containsKey(userTopic.getTopic())) {
            responseObserver.onNext(Boolean.newBuilder().setBoolean(false).build());
            responseObserver.onCompleted();
        }
        if(subscribers.get(userTopic.getTopic()).contains(userTopic.getUser())) {
            subscribers.get(userTopic.getTopic()).remove(userTopic.getUser());
            responseObserver.onNext(Boolean.newBuilder().setBoolean(true).build());
            responseObserver.onCompleted();
        } else {
            responseObserver.onNext(Boolean.newBuilder().setBoolean(false).build());
            responseObserver.onCompleted();
        }
    }

    @Override
    public void listSubscribers(Topic topic, StreamObserver<ListString> responseObserver){
        System.out.println("listSubscribers");
        responseObserver.onNext(ListString.newBuilder().addAllString(subscribers.get(topic.getTopic())).build());
        responseObserver.onCompleted();
    }

    @Override
    public void listNodes(Empty request, StreamObserver<ListString> responseObserver) {
        System.out.println("listNodes");
        responseObserver.onNext(ListString.newBuilder().addAllString(nodes).build());
        responseObserver.onCompleted();
    }
}