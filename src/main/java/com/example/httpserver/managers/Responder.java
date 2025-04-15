package com.example.httpserver.managers;

import com.example.httpserver.responses.GetResponse;
import com.example.httpserver.responses.PostResponse;
import com.example.httpserver.util.IOUtil;

import java.io.*;
import java.net.Socket;

import static com.example.httpserver.responses.Respond404.respondWith404;

public final class Responder {
    private final Socket socket;
    private final GetResponse getResponse;
    private final PostResponse postResponse;

    public Responder(Socket socket) throws IOException {
        this.socket = socket;
        IOUtil ioUtil = IOUtil.getINSTANCE();

        getResponse = new GetResponse(ioUtil);
        postResponse = new PostResponse(ioUtil);

        checkPathAndRespond();
    }

    private void checkPathAndRespond() throws IOException {
        InputStream in = socket.getInputStream();
        String request = extractString(in);
        System.out.println("Request received: \n" + request + "\n");
        System.out.println("Analyzing request type...");
        if (request.contains("GET")) {
            String requestPath = request.substring(0, request.indexOf("HTTP/1.1")).replace("GET", "").trim();
            switch (requestPath) {
                case "/", "" -> getResponse.respondWith200(socket);
                case String s when s.startsWith("/echo/") -> getResponse.respondWithBody200(socket, requestPath);
                case "/user-agent" -> getResponse.respondWithUserAgent200(socket, request);
                case String s when s.startsWith("/files/") -> getResponse.respondWithFile200(socket, requestPath);
                default -> respondWith404(socket);
            }
        } else if (request.contains("POST")) {
            String requestPath = request.substring(0, request.indexOf("HTTP/1.1")).replace("POST", "").trim();
            switch (requestPath) {
                case String s when s.startsWith("/files/") -> postResponse.postFileByRequest(requestPath, request, socket);
                default -> respondWith404(socket);
            }
        }
    }

    private String extractString(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        int contentLength = 0;
        String line;
        while ((line = br.readLine()) != null && !line.isEmpty()) {
            sb.append(line).append("\r\n");

            if (line.toLowerCase().startsWith("content-length:")) {
                contentLength = Integer.parseInt(line.split(":")[1].trim());
            }
        }

        sb.append("\r\n"); // end of headers
        if (contentLength > 0) {
            char[] chars = new char[contentLength];
            int read = br.read(chars, 0, contentLength);
            sb.append(chars, 0, read);
        }
        return sb.toString();
    }
}