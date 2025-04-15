package com.example.httpserver;

import com.example.httpserver.managers.SocketManager;

import java.io.*;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {
    private static final int THREAD_POOL_SIZE = 5;

    public static void main(String[] args) {
        // You can use print statements as follows for debugging, they'll be visible when running tests.
        System.out.println("Logs from your program will appear here!");
        try {
            start();
        } catch (IOException e) {
            System.out.println("bad exception happened");
            throw new RuntimeException(e);
        }
    }

    static void start() throws IOException {
        try (ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
             ServerSocket serverSocket = new ServerSocket(4221)) {

            serverSocket.setReuseAddress(true);

            SocketManager sM = new SocketManager(threadPool, serverSocket);
            sM.accept(); // while(true) tP.exec();
        }
    }
}