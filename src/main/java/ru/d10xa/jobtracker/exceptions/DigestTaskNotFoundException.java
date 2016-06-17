package ru.d10xa.jobtracker.exceptions;

public class DigestTaskNotFoundException extends IllegalArgumentException {

    public DigestTaskNotFoundException() {
    }

    public DigestTaskNotFoundException(String s) {
        super(s);
    }
}
