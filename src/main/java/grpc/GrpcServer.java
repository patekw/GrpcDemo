package grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GrpcServer {



    public static void main(String[] args) {
        Server server = ServerBuilder.forPort(8080).addService(new MessageServiceImpl()).build();


        try {
            server.start();
            System.out.println("server start");
            server.awaitTermination();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
