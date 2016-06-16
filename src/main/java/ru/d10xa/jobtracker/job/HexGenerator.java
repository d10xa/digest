package ru.d10xa.jobtracker.job;

import java.io.IOException;

public interface HexGenerator {

    String generate(DigestData digestData) throws IOException;

}
