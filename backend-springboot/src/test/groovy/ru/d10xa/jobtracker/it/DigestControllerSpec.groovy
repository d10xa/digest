package ru.d10xa.jobtracker.it

import groovy.json.JsonSlurper
import okhttp3.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.http.HttpStatus
import ru.d10xa.jobtracker.Application
import spock.lang.Specification
import spock.lang.Unroll

@SpringApplicationConfiguration(Application)
@WebIntegrationTest(randomPort = true)
@Unroll
class DigestControllerSpec extends Specification {

    @Value('${local.server.port}')
    String port

    OkHttpClient client = new OkHttpClient()

    MediaType jsonType = MediaType.parse("application/json")

    def 'tasks missing'() {
        given:
        Request request = new Request.Builder()
                .url("http://localhost:${port}/digest/tasks")
                .build()
        when:
        Response response = client.newCall(request).execute()

        then:
        response.body().string() == '{"tasks":[]}'
    }

    def 'cancel non-existent task'() {
        given:
        Request request = new Request.Builder()
                .post(RequestBody.create(jsonType, ''))
                .url("http://localhost:${port}/digest/tasks/111111/cancel")
                .build()
        when:
        Response response = client.newCall(request).execute()

        then:
        response.code() == HttpStatus.NOT_FOUND.value()
    }

    def 'schedule task'() {
        given:
        String fileUri = "file://" + getClass().getResource("/testfiles/hosts").toURI().toURL().path
        String jsessionCookie = createJsessionIdCookie()

        when:
        schedule(jsessionCookie, """{"algo":"md5","src":"$fileUri"}""")
        sleep(200)
        Response response2 = getTasks(jsessionCookie)
        String json = response2.body().string()

        then:
        json.contains '"hash":"b076bf46adc34655e5c05f545c66879f"'
        json.contains '"status":"SUCCESS"'
    }

    @Unroll
    def 'schedule task without #absentee'() {
        when:
        Response response = schedule(createJsessionIdCookie(), json)

        then:
        response.code() == HttpStatus.BAD_REQUEST.value()

        where:
        json                          | absentee
        '{"algo":"md5"}'              | 'src'
        '{"src":"file:///etc/hosts"}' | 'algo'
        '{}'                          | 'all'
    }

    def 'cancel and delete with sleeping url'() {
        given: "resource with 1 second sleep time"

        String sleepUri = "http://localhost:$port/sleep-uri?milliseconds=1000"
        def jsessionId = createJsessionIdCookie()

        when: "schedule task"
        schedule(jsessionId, """{"algo":"md5","src":"$sleepUri"}""")

        and: "wait for start processing"
        sleep(40)
        String getTasksResponseBody = getTasks(jsessionId).body().string()

        then: "status IN_PROCESS"
        getTasksResponseBody.contains '"status":"IN_PROCESS"'

        when: "cancel task"
        String taskId = new JsonSlurper().parseText(getTasksResponseBody).tasks[0].id
        assert !taskId.empty

        Response cancelResponse = cancelTask(jsessionId, taskId)

        assert cancelResponse.code() == HttpStatus.OK.toString() as Integer

        sleep(100)
        getTasksResponseBody = getTasks(jsessionId).body().string()

        then: "task interrupted"
        getTasksResponseBody.contains('"status":"INTERRUPTED"')

        when: "delete task"
        deleteTask(jsessionId, taskId)

        then: "task deleted"
        getTasks(jsessionId).body().string() == '{"tasks":[]}'
    }

    Response getTasks(String jsessionIdCookieString) {
        Request request = new Request.Builder()
                .url("http://localhost:${port}/digest/tasks")
                .header("Cookie", jsessionIdCookieString)
                .build()
        client.newCall(request).execute()
    }

    Response schedule(String jsessionIdCookieString, String json) {
        Request request = new Request.Builder()
                .post(RequestBody.create(jsonType, json))
                .header("Cookie", jsessionIdCookieString)
                .url("http://localhost:${port}/digest/schedule")
                .build()
        client.newCall(request).execute()
    }

    Response cancelTask(String jsessionIdCookieString, String taskId) {
        Request cancelRequest = new Request.Builder()
                .post(RequestBody.create(jsonType, ''))
                .header("Cookie", jsessionIdCookieString)
                .url("http://localhost:${port}/digest/tasks/$taskId/cancel")
                .build()
        client.newCall(cancelRequest).execute()
    }

    Response deleteTask(String jsessionIdCookieString, String taskId) {
        Request request = new Request.Builder()
                .delete(RequestBody.create(jsonType, ''))
                .header("Cookie", jsessionIdCookieString)
                .url("http://localhost:${port}/digest/tasks/$taskId")
                .build()
        client.newCall(request).execute()
    }

    String createJsessionIdCookie() {
        Request request = new Request.Builder()
                .url("http://localhost:${port}/session/id")
                .build()
        Response response = client.newCall(request).execute()
        response.header('Set-Cookie')
    }

}
