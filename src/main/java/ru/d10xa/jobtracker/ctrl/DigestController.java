package ru.d10xa.jobtracker.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.d10xa.jobtracker.exceptions.DigestTaskNotFoundException;
import ru.d10xa.jobtracker.job.DigestData;
import ru.d10xa.jobtracker.job.DigestTasksContainer;
import ru.d10xa.jobtracker.service.HexService;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class DigestController {

    private final HexService hexService;

    private final DigestTaskViewMapper digestTaskViewMapper;

    @Autowired
    public DigestController(HexService hexService, DigestTaskViewMapper digestTaskViewMapper) {
        this.hexService = hexService;
        this.digestTaskViewMapper = digestTaskViewMapper;
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
                .map(this.digestTaskViewMapper)
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

}
