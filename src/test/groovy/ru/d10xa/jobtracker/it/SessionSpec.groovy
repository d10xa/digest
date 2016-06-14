package ru.d10xa.jobtracker.it

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
class SessionSpec extends Specification {

    @Value('${local.server.port}')
    String port

    OkHttpClient client = new OkHttpClient()

    def 'check id equal with cookies'() {
        when:
        Request request1 = new Request.Builder()
                .url(url)
                .build()

        Response response1 = client.newCall(request1).execute()
        String json1 = response1.body().string()

        Request request2 = new Request.Builder()
                .url(url)
                .header("Cookie", response1.header('Set-Cookie'))
                .build()

        Response response2 = client.newCall(request2).execute()
        String json2 = response2.body().string()

        then:
        json1 == json2
        json1.startsWith('{"id":')
        json1.length() == """{"id":""}""".length() + 32
    }

    def 'check id various without cookies'() {
        when:
        Request request1 = new Request.Builder()
                .url(url)
                .build()

        String json1 = client.newCall(request1).execute().body().string()

        Request request2 = new Request.Builder()
                .url(url)
                .build()

        String json2 = client.newCall(request2).execute().body().string()

        then:
        json1 != json2
        json1.startsWith('{"id":')
        json1.length() == """{"id":""}""".length() + 32
    }

    String getUrl(){
        "http://localhost:${port}/session/id"
    }

}
