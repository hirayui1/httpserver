package com.example.httpserver.responses;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public final class Respond404 {

    public static void respondWith404(Socket socket) throws IOException {
        OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream());
        String response = "HTTP/1.1 404 Not Found\r\n\r\n";
        System.out.println("Responding with: \n" + response);
        out.write(response);
        out.flush();
        out.close();
    }
}
