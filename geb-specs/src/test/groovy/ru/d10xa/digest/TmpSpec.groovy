package ru.d10xa.digest

import geb.spock.GebReportingSpec

class TmpSpec extends GebReportingSpec {

    def 'button displayed'() {
        go "/src/index.html"

        expect:
        $("button", text: "increment").displayed
    }

}
