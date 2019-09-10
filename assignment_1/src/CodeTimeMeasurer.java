public class CodeTimeMeasurer {

    public static long measureTimeOfFunction(Runnable code) {
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

}
