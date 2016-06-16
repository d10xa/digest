package ru.d10xa.jobtracker.service;

import ru.d10xa.jobtracker.job.DigestData;
import ru.d10xa.jobtracker.job.DigestTasksContainer;

public interface HexService {

    void schedule(String id, DigestData digestData);

    DigestTasksContainer getTasksContainer(String id);

}
