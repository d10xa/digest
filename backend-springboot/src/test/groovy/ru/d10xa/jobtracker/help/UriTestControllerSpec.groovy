package ru.d10xa.jobtracker.help

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.boot.test.WebIntegrationTest
import ru.d10xa.jobtracker.Application
import spock.lang.Specification

@SpringApplicationConfiguration(Application)
@WebIntegrationTest(randomPort = true)
class UriTestControllerSpec extends Specification {

    @Value('${local.server.port}')
    String port

    OkHttpClient client = new OkHttpClient()

    def 'check request with default sleep'() {
        when:
        Request request = new Request.Builder()
                .get()
                .url("http://localhost:${port}/sleep-uri")
                .build()

        Response response = client.newCall(request).execute()

        then:
        response.body().string() == '{"answer":"42"}'
    }

}
