package ru.d10xa.jobtracker.job;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;

public class DigestTasksContainer {

    private final String id;
    private final ExecutorService executorService;
    private final HexGenerator hexGenerator;

    private final List<DigestTask> tasks = new CopyOnWriteArrayList<>();

    public DigestTasksContainer(String id, ExecutorService executorService, HexGenerator hexGenerator) {
        this.id = id;
        this.executorService = executorService;
        this.hexGenerator = hexGenerator;
    }

    public void add(DigestData digestData) {
        DigestTask task = new DigestTask(digestData, hexGenerator);
        this.tasks.add(task);
        executorService.submit(task);
    }

    public List<DigestTask> getTasks() {
        return tasks;
    }

    public void cancel(String taskId) {
        findByTaskId(taskId).cancel(true);
    }

    private DigestTask findByTaskId(String taskId) {
        return this.tasks.stream()
                .filter((t) -> Objects.equals(taskId, t.getTaskId()))
                .findAny()
                .orElseThrow(() ->
                        new IllegalArgumentException("task not found by id " + taskId));
    }

    public void remove(String taskId) {
        this.tasks.remove(findByTaskId(taskId));
    }
}
