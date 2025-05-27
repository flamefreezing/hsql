package org.freezing.naive.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping
    public String test(@RequestParam(required = false, name = "userId") Integer userId) {
        try {
            System.out.println(userId);
            System.out.println(userId == null);
            return "test";
        } catch (Exception e) {
            return "error";
        }
    }
}
