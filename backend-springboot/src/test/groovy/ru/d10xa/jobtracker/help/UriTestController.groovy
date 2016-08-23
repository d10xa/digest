package ru.d10xa.jobtracker.help

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class UriTestController {

    @RequestMapping('/sleep-uri')
    Map<String, String> sleepUri(@RequestParam(defaultValue = "100", required = false) Integer milliseconds) {
        sleep(milliseconds)
        return Collections.singletonMap('answer', '42');
    }

}
