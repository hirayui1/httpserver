package com.example.httpserver.util;

import java.io.*;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class IOUtil {
    private static IOUtil INSTANCE;

    private IOUtil() {}

    public static IOUtil getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new IOUtil();
        }

        return INSTANCE;
    }

    public static boolean hasFile(String fileName) throws NullPointerException {
        System.out.println("Checking the existence of requested file...");
        Set<String> dirs = Stream.of(Objects.requireNonNull(new File("/tmp/data/codecrafters.io/http-server-tester").listFiles())) // specific to codecrafters
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());

        System.out.println("files in this folder:");
        for (String s : dirs) {
            System.out.println(s);
        }

        return dirs.contains(fileName);
    }

    public String[] retrieveFileContents(String request) throws IOException {
        System.out.println("Retrieving file contents...");
        File file = new File("/tmp/data/codecrafters.io/http-server-tester/" + request); // specific to codecrafters
        long byteCount = file.length();
        String fileContent = extractContentAsString(file);
        System.out.println("File contents retrieved successfully.");
        return new String[]{String.valueOf(byteCount), fileContent};
    }

    public String retrieveRequestBody(String request) {
        return request.substring(request.lastIndexOf("\r\n")).trim();
    }

    private String extractContentAsString(File file) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
