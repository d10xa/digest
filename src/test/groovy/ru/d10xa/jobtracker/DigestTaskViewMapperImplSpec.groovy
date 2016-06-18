package ru.d10xa.jobtracker

import ru.d10xa.jobtracker.ctrl.DigestTaskView
import ru.d10xa.jobtracker.ctrl.DigestTaskViewMapper
import ru.d10xa.jobtracker.ctrl.DigestTaskViewMapperImpl
import ru.d10xa.jobtracker.job.Algo
import ru.d10xa.jobtracker.job.DigestTask
import ru.d10xa.jobtracker.job.DigestTaskStatus
import spock.lang.Specification

import java.time.temporal.TemporalUnit
import java.util.concurrent.CancellationException
import java.util.concurrent.ExecutionException

class DigestTaskViewMapperImplSpec extends Specification {

    DigestTaskViewMapper mapper = new DigestTaskViewMapperImpl()

    static String taskId = "d73a826e-a5c9-48c4-8859-d7cfcad54701"
    static String md5sum = "b076bf46adc34655e5c05f545c66879f"

    def 'map SUCCESS task'() {
        given:
        DigestTask task = new DigestTask(null, null) {
            DigestTaskStatus getStatus() { DigestTaskStatus.SUCCESS }

            Algo getAlgo() { Algo.md5 }

            URI getSrc() { URI.create("file:///stub_file") }

            String getTaskId() { DigestTaskViewMapperImplSpec.taskId }

            String get() { md5sum }

            long elapsedTime(TemporalUnit unit) { 2 }

            boolean isDone() { true }
        }

        when:
        DigestTaskView view = mapper.apply(task)

        then:
        view.id == taskId
        view.hash == md5sum
        view.status == DigestTaskStatus.SUCCESS
        view.algo == Algo.md5
        view.elapsedSeconds == 2L
        view.src == "file:///stub_file"
        view.stacktrace == null
    }

    def 'map EXCEPTIONAL task'() {
        given:
        DigestTask task = new DigestTask(null, null) {
            DigestTaskStatus getStatus() { DigestTaskStatus.EXCEPTIONAL }

            Algo getAlgo() { Algo.md5 }

            URI getSrc() { URI.create("not_absolute_uri") }

            String getTaskId() { DigestTaskViewMapperImplSpec.taskId }

            String get() {
                try {
                    return getSrc().toURL()
                } catch (IllegalArgumentException e){
                    throw new ExecutionException(e);
                }
            }

            long elapsedTime(TemporalUnit unit) { 2 }

            boolean isDone() { true }
        }

        when:
        DigestTaskView view = mapper.apply(task)

        then:
        view.status == DigestTaskStatus.EXCEPTIONAL
        view.id == taskId
        view.hash == null
        view.algo == Algo.md5
        view.elapsedSeconds == 2L
        view.src == "not_absolute_uri"
        view.stacktrace.contains "java.lang.IllegalArgumentException: URI is not absolute"
        view.stacktrace.contains "at java.net.URI.toURL"
    }

    def 'map IN_PROCESS task'() {
        given:
        DigestTask task = new DigestTask(null, null) {
            DigestTaskStatus getStatus() { DigestTaskStatus.IN_PROCESS }

            Algo getAlgo() { Algo.md5 }

            URI getSrc() { URI.create("file:///stub_file") }

            String getTaskId() { DigestTaskViewMapperImplSpec.taskId }

            String get() { null }

            long elapsedTime(TemporalUnit unit) { 2 }

            boolean isDone() { false }
        }

        when:
        DigestTaskView view = mapper.apply(task)

        then:
        view.status == DigestTaskStatus.IN_PROCESS
        view.id == taskId
        view.hash == null
        view.algo == Algo.md5
        view.elapsedSeconds == 2L
        view.src == "file:///stub_file"
        view.stacktrace == null
    }

    def 'map INTERRUPTED task'() {
        given:
        DigestTask task = new DigestTask(null, null) {
            DigestTaskStatus getStatus() { DigestTaskStatus.INTERRUPTED }

            Algo getAlgo() { Algo.md5 }

            URI getSrc() { URI.create("file:///stub_file") }

            String getTaskId() { DigestTaskViewMapperImplSpec.taskId }

            String get() { throw new CancellationException() }

            long elapsedTime(TemporalUnit unit) { 2 }

            boolean isDone() { true }
        }

        when:
        DigestTaskView view = mapper.apply(task)

        then:
        view.status == DigestTaskStatus.INTERRUPTED
        view.id == taskId
        view.hash == null
        view.algo == Algo.md5
        view.elapsedSeconds == 2L
        view.src == "file:///stub_file"
        view.stacktrace.startsWith("java.util.concurrent.CancellationException")
    }

}
