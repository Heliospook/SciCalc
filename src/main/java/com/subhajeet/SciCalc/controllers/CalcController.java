package com.subhajeet.SciCalc.controllers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalcController {
    @GetMapping("/root")
    public String squareRoot(@RequestParam float x) {
        float result = x; // placeholder logic
        return "" + result;
    }

    @GetMapping("/factorial")
    public String factorial(@RequestParam int x) {
        float result = x; // placeholder logic
        return "" + result;
    }

    @GetMapping("/ln")
    public String naturalLog(@RequestParam float x) {
        float result = x; // placeholder logic
        return "" + result;
    }

    @GetMapping("/power")
    public String exponentiation(@RequestParam float x, @RequestParam float b) {
        float result = x * b; // placeholder logic
        return "" + result;
    }
}
