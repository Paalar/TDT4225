import java.util.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class write {

    private static final int BLOCKSIZE = 8192;
    private static final long NBLOCKS = 131072;

    private static final int[] fileSizes = {1, 2, 4, 8, 16, 32};
    private static List<Long> executedTime = new ArrayList<>();

    private static void writeFile(long fileSize) throws IOException {
        Path file = Paths.get(System.getProperty("user.dir"), "myjavadata");
        SeekableByteChannel out = Files.newByteChannel(file, EnumSet.of(CREATE, APPEND));

        for (int i = 1; i < fileSize * NBLOCKS; i++) {
            ByteBuffer buffer = ByteBuffer.allocate(BLOCKSIZE);
            out.write(buffer);
        }
    }

    private static long measureTimeOfFunction(Runnable code) {
        long startTime = System.nanoTime();
        try {
            code.run();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        long endTime = System.nanoTime();
        System.out.println(String.format("Start time: %d\nEnd time: %d", startTime, endTime));
        return (endTime - startTime);
    }

    public static void main(String[] args) {
        for (int fileSize : fileSizes) {
            executedTime.add(measureTimeOfFunction(() -> {
                try {
                    writeFile(fileSize);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));
        }
        for (int index = 0; index < executedTime.size(); index++) {
            long time = executedTime.get(index);
            long fileSize = (fileSizes[index] * 1024 * 1024);
            long throughput = (fileSize / TimeUnit.NANOSECONDS.toMillis(time));
            System.out.print(String.format("The time for %d GB was %d ms, and the throughput is MB/s %d\n",
                    fileSizes[index], TimeUnit.NANOSECONDS.toMillis(time), throughput));
        }
    }

}
