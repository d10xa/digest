package ru.d10xa.jobtracker;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URI;

public class Digest {

    private final Algo algo;

    private final URI src;

    private String evaluatedHash;

    public Digest(Algo algo, URI src) {
        this.algo = algo;
        this.src = src;
    }

    public String getHash() throws IOException {
        byte[] bytes = IOUtils.toByteArray(src);
        if (this.evaluatedHash == null) {
            this.evaluatedHash = algo.eval(bytes);
        }
        return this.evaluatedHash;
    }

}
