package ru.d10xa.jobtracker.ctrl;

import ru.d10xa.jobtracker.job.Algo;
import ru.d10xa.jobtracker.job.DigestTaskStatus;

public class DigestTaskView {

    private String id;
    private Algo algo;
    private String src;
    private String hash;
    private String stacktrace;
    private DigestTaskStatus status;
    private Long elapsedSeconds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Algo getAlgo() {
        return algo;
    }

    public void setAlgo(Algo algo) {
        this.algo = algo;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getStacktrace() {
        return stacktrace;
    }

    public void setStacktrace(String stacktrace) {
        this.stacktrace = stacktrace;
    }

    public DigestTaskStatus getStatus() {
        return status;
    }

    public void setStatus(DigestTaskStatus status) {
        this.status = status;
    }

    public Long getElapsedSeconds() {
        return elapsedSeconds;
    }

    public void setElapsedSeconds(Long elapsedSeconds) {
        this.elapsedSeconds = elapsedSeconds;
    }
}
