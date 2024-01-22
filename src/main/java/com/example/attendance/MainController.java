package com.example.attendance;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
//http://localhost:8080/swagger-ui/index.html
@Controller
public class MainController {
    @GetMapping("/")
    @ResponseBody
    public String root() {
        return "메인화면 입니다.";
    }

    @GetMapping("/test")
    @ResponseBody
    public String root2() {
        return "테스트화면 입니다.";
    }
}
