import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardOpenOption.READ;

public class Read {

    private final static int BLOCK_SIZE = BlockSize.BLOCK_SIZE.getBlock();
    private final static int N_BLOCKS = BlockSize.N_BLOCKS.getBlock();
    private static final int[] fileSizes = {1, 2, 4, 8, 16, 32};
    private static List<Long> executedTime = new ArrayList<>();

    private static void readFile(String filename, int fileSize) throws IOException {
        Path file = Paths.get(System.getProperty("user.dir"), filename);
        SeekableByteChannel in = Files.newByteChannel(file, EnumSet.of(READ));
        ByteBuffer buffer = ByteBuffer.allocate(BLOCK_SIZE);
        try {
            for (int i = 0; i < fileSize * N_BLOCKS; i++) {
                in.read(buffer);
                buffer.clear();
            }
            System.out.println(String.format("File %s read", filename));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        for (int fileSize : fileSizes) {
            executedTime.add(CodeTimeMeasurer.measureTimeOfFunction(() -> {
                try {
                    readFile(String.format("filesize_%d_gig", fileSize), fileSize);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));
        }

        for (int index = 0; index < executedTime.size(); index++) {
            long time = executedTime.get(index);
            long fileSize = (fileSizes[index] * 1024 * 1024);
            long throughput = (fileSize / TimeUnit.NANOSECONDS.toMillis(time));
            System.out.print(String.format("The time for %d GB was %d ms, and the read throughput is MB/s %d\n",
                    fileSizes[index], TimeUnit.NANOSECONDS.toMillis(time), throughput));
        }
    }

}
