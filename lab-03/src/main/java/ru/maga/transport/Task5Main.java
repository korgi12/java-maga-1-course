package ru.maga.transport;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public final class Task5Main {
    public static void main(String[] args) throws Exception {
        Path directory = Path.of("marks");
        if (!Files.isDirectory(directory)) {
            directory = Path.of("lab-03", "marks");
        }
        BlockingQueue<Transport> queue = new ArrayBlockingQueue<>(2);
        for (int i = 0; i < 5; i++) {
            Path file = directory.resolve("mark-" + i + ".txt");
            new Thread(new FileTransportProducer(file, queue)).start();
        }
        for (int i = 0; i < 5; i++) {
            System.out.println(queue.take().getMark());
        }
    }
}
