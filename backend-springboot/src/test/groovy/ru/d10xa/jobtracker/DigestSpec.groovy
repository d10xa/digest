package ru.d10xa.jobtracker

import ru.d10xa.jobtracker.job.Algo
import ru.d10xa.jobtracker.job.DigestData
import ru.d10xa.jobtracker.job.HexGenerator
import ru.d10xa.jobtracker.job.HexGeneratorImpl
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class DigestSpec extends Specification {

    static URL hostsFile = getClass().getResource("/testfiles/hosts")

    HexGenerator hexGenerator = new HexGeneratorImpl()

    /**
     * Test data generated via:
     *
     * /usr/bin/md5sum
     * /usr/bin/sha1sum
     * /usr/bin/sha256sum
     */
    @Unroll
    def 'check #algo hash'() {
        given:
        DigestData digest = new DigestData(algo, hostsFile.toURI())

        when:
        String hash = hexGenerator.generate(digest)

        then:
        hash == expectedHash

        where:
        algo        || expectedHash
        Algo.md5    || 'b076bf46adc34655e5c05f545c66879f'
        Algo.sha1   || 'e1397afec9f2541fe03abbd39c844fbaffebbb17'
        Algo.sha256 || 'dcf8613a352b57d92aa0182917eb348a2c73d747812f5a1120aa73bd25f04bb9'

    }

}
