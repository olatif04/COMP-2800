import java.io.File;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class DirectorySizeForkJoin extends RecursiveTask<Long> {
    private final File directory;

    public DirectorySizeForkJoin(File directory) {
        this.directory = directory;
    }

    @Override
    protected Long compute() {
        long size = 0;
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    size += file.length();
                } else {
                    DirectorySizeForkJoin task = new DirectorySizeForkJoin(file);
                    task.fork();
                    size += task.join();
                }
            }
        }
        return size;
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java DirectorySizeForkJoin <directory_path>");
            System.exit(1);
        }
        ForkJoinPool pool = new ForkJoinPool();
        DirectorySizeForkJoin task = new DirectorySizeForkJoin(new File(args[0]));
        long startTime = System.currentTimeMillis();
        long size = pool.invoke(task);
        long endTime = System.currentTimeMillis();
        System.out.printf("Directory size: %d bytes%n", size);
        System.out.printf("Time taken: %d milliseconds%n", endTime - startTime);
    }
}
