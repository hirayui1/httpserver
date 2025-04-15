package com.example.httpserver.responses;

import com.example.httpserver.util.IOUtil;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class PostResponse {
    private final IOUtil ioUtil;

    public PostResponse(IOUtil ioUtil) {
        this.ioUtil = ioUtil;
    }

    public void postFileByRequest(String requestPath, String request, Socket socket) throws IOException {
        System.out.println("Creating file...");
        requestPath = requestPath.replace("/files/", "").trim();
        String body = ioUtil.retrieveRequestBody(request);
        System.out.println("Body retrieved: " + body);
        System.out.println("Writing into the file...");
        try (BufferedWriter br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("/tmp/data/codecrafters.io/http-server-tester/" + requestPath)))) {
            br.write(body);
        }
        System.out.println("Finished writing to a file.");
        respondWith201(socket);
    }
    private void respondWith201(Socket socket) throws IOException {
        OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream());
        String response = "HTTP/1.1 201 Created\r\n\r\n";
        System.out.println("Responding with: \n" + response);
        out.write(response);
        out.flush();
        out.close();
    }
}