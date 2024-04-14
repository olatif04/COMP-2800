import java.io.File;

public class DirectorySizeSingleThread {

    public static long calculateSize(File directory) {
        long size = 0;
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    size += file.length();
                } else {
                    size += calculateSize(file);
                }
            }
        }
        return size;
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java DirectorySizeSingleThread <directory_path>");
            System.exit(1);
        }
        File directory = new File(args[0]);
        long startTime = System.currentTimeMillis();
        long size = calculateSize(directory);
        long endTime = System.currentTimeMillis();
        System.out.printf("Directory size: %d bytes%n", size);
        System.out.printf("Time taken: %d milliseconds%n", endTime - startTime);
    }
}
