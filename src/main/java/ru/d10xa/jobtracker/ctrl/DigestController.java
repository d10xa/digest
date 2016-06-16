package ru.d10xa.jobtracker.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.d10xa.jobtracker.job.DigestData;
import ru.d10xa.jobtracker.job.DigestTask;
import ru.d10xa.jobtracker.service.HexService;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
        this.hexService.schedule(httpSession.getId(), new DigestData(request.getAlgo(), request.getSrc()));
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/digest/countAll", method = RequestMethod.GET)
    public Map<String, Integer> countAll(HttpSession httpSession) {
        List<DigestTask> tasks = hexService.getTasksContainer(httpSession.getId()).getTasks();
        return Collections.singletonMap("countAll", tasks.size());
    }

}
