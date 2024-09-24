package com.subhajeet.SciCalc.controllers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.lang.Math;

@RestController
public class CalcController {
    @GetMapping("/root")
    public String squareRoot(@RequestParam double x) {;
        if(x >= 0){
            return String.format("%.2f", Math.sqrt(x));
        }
        return "Negative input to square root";
    }

    @GetMapping("/factorial")
    public String factorial(@RequestParam int x) {
        if(x > 10){
            return "Input too large for factorial";
        }
        if(x < 0){
            return "Factorial not defined for negative integers";
        }
        int ret = 1;
        for(int i = 1; i <= x; i++) ret *= i;
        return String.format("%d", ret);
    }

    @GetMapping("/ln")
    public String naturalLog(@RequestParam double x) {
        if(x > 0){
            return String.format("%.2f", Math.log(x));
        }
        return "Negative/Zero input to natural logarithm";
    }

    @GetMapping("/power")
    public String exponentiation(@RequestParam double x, @RequestParam double b) {
        return String.format("%.2f", Math.pow(x, b));
    }
}
