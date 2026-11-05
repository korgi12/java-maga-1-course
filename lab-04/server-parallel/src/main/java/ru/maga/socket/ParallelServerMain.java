package ru.maga.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ParallelServerMain {
    private ParallelServerMain() {
    }

    public static void main(String[] args) throws IOException {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 5001;
        int workers = args.length > 1 ? Integer.parseInt(args[1]) : 8;
        if (workers < 1) throw new IllegalArgumentException("workers must be positive");
        ExecutorService pool = Executors.newFixedThreadPool(workers);
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("Parallel server started on " + port);
            while (true) {
                Socket client = server.accept();
                pool.execute(() -> {
                    try {
                        ServerHandler.handle(client);
                    } catch (Exception exception) {
                        System.err.println(exception.getMessage());
                    }
                });
            }
        } finally {
            pool.shutdown();
        }
    }
}
