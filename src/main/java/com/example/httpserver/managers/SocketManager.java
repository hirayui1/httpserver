package com.example.httpserver.managers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public final class SocketManager {
    ExecutorService threadPool;
    ServerSocket serverSocket;


    public SocketManager(ExecutorService threadPool, ServerSocket serverSocket) {
        this.threadPool = threadPool;
        this.serverSocket = serverSocket;
    }

    public void accept() throws IOException {
        while (true) {
            Socket socket = serverSocket.accept();
            threadPool.execute(() -> {
                try {
                    new Responder(socket);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

}
