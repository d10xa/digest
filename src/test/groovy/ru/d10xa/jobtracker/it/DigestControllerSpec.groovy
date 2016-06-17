package ru.d10xa.jobtracker.it

import okhttp3.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.boot.test.WebIntegrationTest
import ru.d10xa.jobtracker.Application
import spock.lang.Specification

@SpringApplicationConfiguration(Application)
@WebIntegrationTest(randomPort = true)
class DigestControllerSpec extends Specification {

    @Value('${local.server.port}')
    String port

    OkHttpClient client = new OkHttpClient()

    MediaType jsonType = MediaType.parse("application/json")

    def 'schedule task'() {
        given:
        String fileUri = "file://" + getClass().getResource("/testfiles/hosts").toURI().toURL().path

        when:
        Request request1 = new Request.Builder()
                .post(RequestBody.create(jsonType, """{"algo":"md5","src":"$fileUri"}"""))
                .url("http://localhost:${port}/digest/schedule")
                .build()

        Response response1 = client.newCall(request1).execute()
        def cookie = response1.header('Set-Cookie')

        Request request2 = new Request.Builder()
                .url("http://localhost:${port}/digest/tasks")
                .header("Cookie", cookie)
                .build()

        sleep(50)
        Response response2 = client.newCall(request2).execute()
        String json = response2.body().string()

        then:
        json.contains '"hash":"b076bf46adc34655e5c05f545c66879f"'
        json.contains '"status":"SUCCESS"'
    }

}
