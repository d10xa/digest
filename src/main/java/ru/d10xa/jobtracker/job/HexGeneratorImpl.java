package ru.d10xa.jobtracker.job;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class HexGeneratorImpl implements HexGenerator {

    @Override
    public String generate(DigestData digestData) throws IOException {
        URI uri = digestData.getSrc();
        InputStream is = uri.toURL().openStream();
        String hex;
        switch (digestData.getAlgo()) {
            case md5:
                hex = DigestUtils.md5Hex(is);
                break;
            case sha1:
                hex = DigestUtils.sha1Hex(is);
                break;
            case sha256:
                hex = DigestUtils.sha256Hex(is);
                break;
            default:
                throw new IllegalArgumentException("Unknown algorithm " + digestData.getAlgo());
        }
        return hex;
    }

}
