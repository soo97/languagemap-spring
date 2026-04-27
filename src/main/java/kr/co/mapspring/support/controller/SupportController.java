package kr.co.mapspring.support.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class SupportController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello!";
    }
}