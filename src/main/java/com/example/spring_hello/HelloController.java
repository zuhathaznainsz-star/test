package com.example.spring_hello;

import org.springframework.web.bind.annotation.
GetMapping;
import org.springframework.web.bind.annotation.
RestController;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class HelloController {
@GetMapping("/")
public String hello() {
 return "Hello World from Spring Boot!";
}

}
