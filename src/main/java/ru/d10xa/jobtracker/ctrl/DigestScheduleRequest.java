package ru.d10xa.jobtracker.ctrl;

import ru.d10xa.jobtracker.job.Algo;

import java.net.URI;

public class DigestScheduleRequest {

    private Algo algo;

    private URI src;

    public Algo getAlgo() {
        return algo;
    }

    public DigestScheduleRequest setAlgo(Algo algo) {
        this.algo = algo;
        return this;
    }

    public URI getSrc() {
        return src;
    }

    public DigestScheduleRequest setSrc(URI src) {
        this.src = src;
        return this;
    }
}
