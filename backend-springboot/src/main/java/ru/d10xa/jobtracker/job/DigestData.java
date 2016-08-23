package ru.d10xa.jobtracker.job;

import java.net.URI;

public class DigestData {

    private final Algo algo;

    private final URI src;

    public DigestData(Algo algo, URI src) {
        this.algo = algo;
        this.src = src;
    }

    public Algo getAlgo() {
        return algo;
    }

    public URI getSrc() {
        return src;
    }
}
