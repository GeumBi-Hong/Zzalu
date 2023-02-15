package com.samsamoo.zzalu.global.advice;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController implements ErrorController {
    private final String ERROR_PATH = "/error";

    @GetMapping(ERROR_PATH)
    public String redirectRoot(){
        return "/static/index.html";
    }

    public String getErrorPath(){
        return null;
    }
}
