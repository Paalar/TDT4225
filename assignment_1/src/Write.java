import java.util.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class Write {
    private static final int[] fileSizes = {1, 2, 4, 8, 16, 32};
    private static List<Long> executedTime = new ArrayList<>();

    private final static int BLOCK_SIZE = BlockSize.BLOCK_SIZE.getBlock();
    private final static int N_BLOCKS = BlockSize.N_BLOCKS.getBlock();

    private static void writeFile(long fileSize, String filename) throws IOException {
        Path file = Paths.get(System.getProperty("user.dir"), filename);
        SeekableByteChannel out = Files.newByteChannel(file, EnumSet.of(CREATE, APPEND));

        for (int i = 1; i < fileSize * N_BLOCKS ; i++) {
            ByteBuffer buffer = ByteBuffer.allocate(BLOCK_SIZE);
            out.write(buffer);
        }
    }

    public static void main(String[] args) {
        for (int fileSize : fileSizes) {
            executedTime.add(CodeTimeMeasurer.measureTimeOfFunction(() -> {
                try {
                    writeFile(fileSize, String.format("filesize_%d_gig", fileSize));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));
        }
        for (int index = 0; index < executedTime.size(); index++) {
            long time = executedTime.get(index);
            long fileSize = (fileSizes[index] * 1024 * 1024);
            long throughput = (fileSize / TimeUnit.NANOSECONDS.toMillis(time));
            System.out.print(String.format("The time for %d GB was %d ms, and the write throughput is MB/s %d\n",
                    fileSizes[index], TimeUnit.NANOSECONDS.toMillis(time), throughput));
        }
    }
}