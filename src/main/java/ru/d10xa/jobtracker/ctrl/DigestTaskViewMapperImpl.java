package ru.d10xa.jobtracker.ctrl;

import org.springframework.stereotype.Component;
import ru.d10xa.jobtracker.job.DigestTask;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

@Component
public class DigestTaskViewMapperImpl implements DigestTaskViewMapper {

    @Override
    public DigestTaskView apply(DigestTask task) {
        DigestTaskView view = new DigestTaskView();
        view.setId(task.getTaskId());
        view.setAlgo(task.getAlgo());
        view.setSrc(task.getSrc().toString());
        view.setStatus(task.getStatus());
        view.setElapsedSeconds(task.elapsedTime(ChronoUnit.SECONDS));
        if (task.isDone()) {
            try {
                view.setHash(String.valueOf(task.get()));
            } catch (CancellationException | InterruptedException e) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                view.setStacktrace(sw.toString());
            } catch (ExecutionException e) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.getCause().printStackTrace(pw);
                view.setStacktrace(sw.toString());
            }
        }
        return view;
    }

}
