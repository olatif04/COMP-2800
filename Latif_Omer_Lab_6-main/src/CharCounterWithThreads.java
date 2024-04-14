import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CharCounterWithThreads {
    private String filePath;
    private int numberOfThreads;
    private int characterCount = 0;
    private Lock lock = new ReentrantLock();
    private Condition done = lock.newCondition();
    private int activeThreads = 0;

    public CharCounterWithThreads(String filePath, int numberOfThreads) {
        this.filePath = filePath;
        this.numberOfThreads = numberOfThreads;
    }

    public void countCharacters() throws Exception {
        long fileSize = Files.size(Paths.get(filePath));
        long chunkSize = fileSize / numberOfThreads;

        activeThreads = numberOfThreads; // Initialize activeThreads with the number of threads to start

        for (int i = 0; i < numberOfThreads; i++) {
            long start = i * chunkSize;
            long end = (i == numberOfThreads - 1) ? fileSize : (start + chunkSize - 1);
            Thread worker = new Thread(new Worker(start, end));
            worker.start();
        }

        lock.lock();
        try {
            while (activeThreads > 0) {
                done.await();
            }
        } finally {
            lock.unlock();
        }
    }

    private class Worker implements Runnable {
        private long start;
        private long end;

        public Worker(long start, long end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public void run() {
            try (FileReader reader = new FileReader(filePath)) {
                if (start > 0) {
                    reader.skip(start);
                }

                long currentPosition = start;
                while (currentPosition < end && reader.read() != -1) {
                    lock.lock();
                    try {
                        characterCount++;
                    } finally {
                        lock.unlock();
                    }
                    currentPosition++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            lock.lock();
            try {
                activeThreads--;
                if (activeThreads == 0) {
                    done.signalAll(); // Use signalAll to ensure all waiting threads are awakened
                }
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Please provide the file path and number of threads.");
            return;
        }
        CharCounterWithThreads counter = new CharCounterWithThreads(args[0], Integer.parseInt(args[1]));
        counter.countCharacters();
        System.out.println("Number of characters: " + counter.characterCount);
    }
}
