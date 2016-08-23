package ru.d10xa.jobtracker.ctrl;

import ru.d10xa.jobtracker.job.DigestTask;

import java.util.function.Function;

public interface DigestTaskViewMapper extends Function<DigestTask, DigestTaskView> {

}
