package ru.d10xa.jobtracker;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Map;

@RestController
public class SessionIdController {

    @RequestMapping("/session/id")
    public Map<String, String> getId(HttpSession httpSession) {
        return Collections.singletonMap("id", httpSession.getId());
    }

}
