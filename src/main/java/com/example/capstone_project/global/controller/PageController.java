package com.example.capstone_project.global.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
public class PageController {
    @GetMapping("/check")
    public void check() {
        throw new ResponseStatusException(HttpStatus.OK);
    }

    @GetMapping("/search")
    public ModelAndView search() {
        return new ModelAndView("html/search");
    }

    @GetMapping("/myPage")
    public ModelAndView myPage () {
        return new ModelAndView("html/myPage");
    }

    @GetMapping("/")
    public ModelAndView main () {
        return new ModelAndView("html/main");
    }

    @GetMapping("/login")
    public ModelAndView login () {
        return new ModelAndView("html/login");
    }

    @GetMapping("/join")
    public ModelAndView join () {
        return new ModelAndView("html/join");
    }

    @GetMapping("/diet")
    public ModelAndView diet () {
        return new ModelAndView("html/diet");
    }

}