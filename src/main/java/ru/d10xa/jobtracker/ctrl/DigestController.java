package ru.d10xa.jobtracker.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.d10xa.jobtracker.exceptions.DigestTaskNotFoundException;
import ru.d10xa.jobtracker.job.DigestData;
import ru.d10xa.jobtracker.job.DigestTask;
import ru.d10xa.jobtracker.job.DigestTasksContainer;
import ru.d10xa.jobtracker.service.HexService;

import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
public class DigestController {

    private final HexService hexService;

    @Autowired
    public DigestController(HexService hexService) {
        this.hexService = hexService;
    }

    @RequestMapping(value = "/digest/schedule", method = RequestMethod.POST)
    public ResponseEntity schedule(
            @RequestBody DigestScheduleRequest request, HttpSession httpSession) {
        if (request.getAlgo() == null || request.getSrc() == null) {
            throw new IllegalArgumentException();
        }
        this.hexService.schedule(httpSession.getId(), new DigestData(request.getAlgo(), request.getSrc()));
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping("/digest/tasks")
    public Map<String, List<DigestTaskView>> listTasks(HttpSession httpSession) {
        DigestTasksContainer tasksContainer = hexService.getTasksContainer(httpSession.getId());
        if (tasksContainer == null) {
            return Collections.singletonMap("tasks", Collections.emptyList());
        }
        List<DigestTaskView> list = tasksContainer.getTasks()
                .stream()
                .map(toView())
                .collect(Collectors.toList());
        Collections.reverse(list);
        return Collections.singletonMap("tasks", list);
    }

    @RequestMapping(value = "/digest/tasks/{taskId}/cancel", method = RequestMethod.POST)
    public ResponseEntity cancel(HttpSession httpSession, @PathVariable String taskId) {
        DigestTasksContainer tasksContainer = hexService.getTasksContainer(httpSession.getId());
        tasksContainer.cancel(taskId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/digest/tasks/{taskId}", method = RequestMethod.DELETE)
    public ResponseEntity delete(HttpSession httpSession, @PathVariable String taskId) {
        DigestTasksContainer tasksContainer = hexService.getTasksContainer(httpSession.getId());
        tasksContainer.remove(taskId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ExceptionHandler(DigestTaskNotFoundException.class)
    public ResponseEntity handleNotFoundException() {
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    private Function<DigestTask, DigestTaskView> toView() {
        return (task) -> {
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
        };
    }

}
