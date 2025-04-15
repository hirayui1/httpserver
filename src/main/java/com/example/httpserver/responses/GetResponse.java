package com.example.httpserver.responses;

import com.example.httpserver.util.IOUtil;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class GetResponse {
    private final IOUtil ioUtil;

    public GetResponse(IOUtil ioUtil) {
        this.ioUtil = ioUtil;
    }

    public void respondWith200(Socket socket) throws IOException {
        OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream());
        String response = "HTTP/1.1 200 OK\r\n\r\n";
        System.out.println("Responding with: \n" + response);
        out.write(response);
        out.flush();
        out.close();
    }

    public void respondWithBody200(Socket socket, String request) throws IOException {
        OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream());
        request = request.replace("/echo/", "").trim();
        System.out.println("Acquiring response body...");
        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/plain\r\n" +
                "Content-Length: " + request.length() + "\r\n" +
                "\r\n" +
                request; // return string
        System.out.println("Responding with: \n" + response);
        out.write(response);
        out.flush();
        out.close();
    }

    public void respondWithUserAgent200(Socket socket, String request) throws IOException {
        OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream());
        System.out.println("Retrieving UserAgent from the request...");
        String userAgent = request.substring(
                request.indexOf("User-Agent:") + "User-Agent:".length(), // from
                request.indexOf("\r", request.indexOf("User-Agent:")) // to
        ).trim(); // get from "User-Agent:" to the next "/r"
        String response = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/plain\r\n"
                + "Content-Length: " + userAgent.length() + "\r\n"
                + "\r\n"
                + userAgent;
        System.out.println("Responding with: \n" + response);
        out.write(response);
        out.flush();
        out.close();
    }

    public void respondWithFile200(Socket socket, String request) throws IOException {
        OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream());
        System.out.println("Retrieving filePath from the request...");
        request = request.replace("/files/", "").trim();
        if (IOUtil.hasFile(request)) {
            System.out.print("File found. ");
            String[] contents = ioUtil.retrieveFileContents(request);
            String response = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: application/octet-stream\r\n" +
                    "Content-Length: " + contents[0] + "\r\n\r\n" +
                    contents[1];

            System.out.println("Responding with: \n" + response);
            out.write(response);
            out.flush();
            out.close();
        } else {
            System.out.println("File does not exist in the path, responding with 404...");
            Respond404.respondWith404(socket);
        }
    }

}
