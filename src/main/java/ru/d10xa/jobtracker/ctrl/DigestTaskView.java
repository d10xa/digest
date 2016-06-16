package ru.d10xa.jobtracker.ctrl;

import ru.d10xa.jobtracker.job.Algo;

public class DigestTaskView {

    private Algo algo;
    private String src;
    private String hash;
    private String stacktrace;
    private Long elapsedSeconds;
    private Boolean cancelled;
    private Boolean done;

    public Algo getAlgo() {
        return algo;
    }

    public DigestTaskView setAlgo(Algo algo) {
        this.algo = algo;
        return this;
    }

    public String getHash() {
        return hash;
    }

    public DigestTaskView setHash(String hash) {
        this.hash = hash;
        return this;
    }

    public String getStacktrace() {
        return stacktrace;
    }

    public DigestTaskView setStacktrace(String stacktrace) {
        this.stacktrace = stacktrace;
        return this;
    }

    public Long getElapsedSeconds() {
        return elapsedSeconds;
    }

    public DigestTaskView setElapsedSeconds(Long elapsedSeconds) {
        this.elapsedSeconds = elapsedSeconds;
        return this;
    }

    public String getSrc() {
        return src;
    }

    public DigestTaskView setSrc(String src) {
        this.src = src;
        return this;
    }

    public Boolean getCancelled() {
        return cancelled;
    }

    public DigestTaskView setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
        return this;
    }

    public Boolean getDone() {
        return done;
    }

    public DigestTaskView setDone(Boolean done) {
        this.done = done;
        return this;
    }
}
