package ru.d10xa.jobtracker.job;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class DigestTask extends FutureTask<String> {

    private final String taskId = UUID.randomUUID().toString();
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private DigestData digestData;
    private DigestTaskStatus status = DigestTaskStatus.NEW;

    public DigestTask(DigestData digestData, HexGenerator hexGenerator) {
        super(new DigestTaskCallable(digestData, hexGenerator));
        this.digestData = digestData;
    }

    private static class DigestTaskCallable implements Callable<String> {

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
        this.status = DigestTaskStatus.IN_PROCESS;
        super.run();
    }

    @Override
    protected void setException(Throwable t) {
        super.setException(t);
        this.status = DigestTaskStatus.EXCEPTIONAL;
    }

    @Override
    protected void set(String string) {
        super.set(string);
        this.status = DigestTaskStatus.SUCCESS;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        boolean cancel = super.cancel(mayInterruptIfRunning);
        this.status = DigestTaskStatus.INTERRUPTED;
        return cancel;
    }

    @Override
    protected void done() {
        this.endTime = LocalDateTime.now();
    }

    public DigestTaskStatus getStatus() {
        return status;
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
