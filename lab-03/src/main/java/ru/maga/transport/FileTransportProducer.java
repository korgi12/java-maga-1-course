package ru.maga.transport;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;

public final class FileTransportProducer implements Runnable {
    private final Path file;
    private final BlockingQueue<Transport> queue;

    public FileTransportProducer(Path file, BlockingQueue<Transport> queue) {
        this.file = file;
        this.queue = queue;
    }

    @Override public void run() {
        try {
            String mark = Files.readAllLines(file, StandardCharsets.UTF_8).get(0);
            queue.put(new Car(mark, 0));
        } catch (Exception ex) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Unable to read " + file, ex);
        }
    }
}
