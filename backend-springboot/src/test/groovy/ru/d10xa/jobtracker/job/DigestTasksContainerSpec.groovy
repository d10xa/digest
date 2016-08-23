package ru.d10xa.jobtracker.job

import spock.lang.Specification

import java.time.temporal.ChronoUnit
import java.util.concurrent.ExecutionException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DigestTasksContainerSpec extends Specification {

    ExecutorService executor
    HexGenerator hexGenerator
    URI uri

    def setup() {
        executor = Executors.newSingleThreadExecutor()
        uri = "".toURI()
    }

    def 'schedule single task'() {
        given:
        hexGenerator = { DigestData -> sleep(100); "42" }
        DigestTasksContainer digestContainer = new DigestTasksContainer(
                '1', executor, hexGenerator)
        when:
        digestContainer.add(new DigestData(Algo.md5, uri))
        DigestTask task = digestContainer.tasks[0]

        then:
        digestContainer.tasks.size() == 1
        !task.isDone()
        task.status.with { it == NEW || it == IN_PROCESS }
        task.get() == "42"
        task.isDone()
        task.elapsedTime(ChronoUnit.MILLIS) > 100
        task.elapsedTime(ChronoUnit.MILLIS) < 150
        task.status == DigestTaskStatus.SUCCESS

        cleanup:
        executor.shutdown()
    }

    def 'schedule task with exception'() {
        given:
        hexGenerator = { DigestData -> throw new IOException("test") }
        DigestTasksContainer digestContainer = new DigestTasksContainer(
                '1', executor, hexGenerator)
        digestContainer.add(new DigestData(Algo.md5, uri))
        DigestTask task = digestContainer.tasks[0]

        when:
        task.get()

        then:
        ExecutionException e = thrown()
        e.cause instanceof IOException
        e.cause.message == 'test'
        task.status == DigestTaskStatus.EXCEPTIONAL

        cleanup:
        executor.shutdown()
    }

    def 'cancel and remove task'() {
        given:
        hexGenerator = { DigestData -> sleep(1000); "42" }
        DigestTasksContainer digestContainer = new DigestTasksContainer(
                '1', executor, hexGenerator)
        digestContainer.add(new DigestData(Algo.md5, uri))
        DigestTask task = digestContainer.tasks[0]

        when:
        digestContainer.cancel(task.getTaskId())

        then:
        task.isCancelled()
        task.isDone()
        task.status == DigestTaskStatus.INTERRUPTED

        and: 'remove task'
        digestContainer.remove(task.getTaskId())

        then:
        digestContainer.getTasks().size() == 0

        cleanup:
        executor.shutdown()
    }

}
