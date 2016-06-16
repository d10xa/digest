package ru.d10xa.jobtracker.job;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class DigestTask extends FutureTask {

    private final String taskId = UUID.randomUUID().toString();
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private DigestData digestData;

    public DigestTask(DigestData digestData, HexGenerator hexGenerator) {
        super(new DigestTaskCallable(digestData, hexGenerator));
        this.digestData = digestData;
    }

    private static class DigestTaskCallable<T> implements Callable<String> {

        private final DigestData digestData;

        private final HexGenerator hexGenerator;

        DigestTaskCallable(DigestData digestData, HexGenerator hexGenerator) {
            this.digestData = digestData;
            this.hexGenerator = hexGenerator;
        }

        @Override
        public String call() throws Exception {
            return hexGenerator.generate(digestData);
        }
    }

    public long elapsedTime(TemporalUnit unit) {
        if (startTime == null) {
            return 0;
        }
        if (endTime == null) {
            return startTime.until(LocalDateTime.now(), unit);
        }
        return startTime.until(endTime, unit);
    }

    @Override
    public void run() {
        this.startTime = LocalDateTime.now();
        super.run();
    }

    @Override
    protected void done() {
        this.endTime = LocalDateTime.now();
    }

    public String getTaskId() {
        return taskId;
    }

    public Algo getAlgo() {
        return digestData.getAlgo();
    }

    public URI getSrc() {
        return digestData.getSrc();
    }
}
