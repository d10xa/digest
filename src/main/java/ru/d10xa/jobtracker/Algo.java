package ru.d10xa.jobtracker;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.function.Function;

public enum Algo {

    md5(DigestUtils::md5Hex),
    sha1(DigestUtils::sha1Hex),
    sha256(DigestUtils::sha256Hex);

    private final Function<byte[], String> evalFunction;

    Algo(Function<byte[], String> function) {
        this.evalFunction = function;
    }

    public String eval(byte[] bytes) {
        return evalFunction.apply(bytes);
    }

}
