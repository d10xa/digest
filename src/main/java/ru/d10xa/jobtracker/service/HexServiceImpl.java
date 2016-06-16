package ru.d10xa.jobtracker.service;

import ru.d10xa.jobtracker.job.DigestData;
import ru.d10xa.jobtracker.job.DigestTasksContainer;
import ru.d10xa.jobtracker.job.HexGenerator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

public class HexServiceImpl implements HexService {

    private final Map<String, DigestTasksContainer> containers = new ConcurrentHashMap<>();
    private final ExecutorService executorService;
    private final HexGenerator hexGenerator;

    public HexServiceImpl(ExecutorService executorService, HexGenerator hexGenerator) {
        this.executorService = executorService;
        this.hexGenerator = hexGenerator;
    }

    @Override
    public void schedule(String id, DigestData digestData) {
        this.containers
                .computeIfAbsent(id, (val) -> createContainer(id))
                .add(digestData);
    }

    private DigestTasksContainer createContainer(String id) {
        return new DigestTasksContainer(id, executorService, hexGenerator);
    }

    @Override
    public DigestTasksContainer getTasksContainer(String id) {
        return containers.get(id);
    }

}
