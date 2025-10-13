package com.strhzy.calc.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    @GetMapping("/")
    public String showCalculator() {
        return "CalcPage";
    }

    @PostMapping("/calculate")
    public String calculate(@RequestParam double num1, @RequestParam double num2, @RequestParam String operation, Model model) {
        double result = 0;
        switch (operation) {
            case "+":
                result = num1 + num2;
                break;
            case "-":
                result = num1 - num2;
                break;
            case "*":
                result = num1 * num2;
                break;
            case "/":
                if (num2 == 0) {
                    model.addAttribute("error", "Деление на ноль невозможно");
                    return "CalcPage";
                }
                result = num1 / num2;
                break;
        }

        model.addAttribute("result", result);

        return "CalcPage";
    }

    @GetMapping("/conv")
    public String showConverter() {
        return "ConvPage";
    }

    @PostMapping("/convert")
    public String convert(@RequestParam double num, @RequestParam String currency, Model model) {
        double result = switch (currency) {
            case "usd" -> num / 90;
            case "eur" -> num / 100;
            default -> 0;
        };
        model.addAttribute("result", result);
        return "ConvPage";
    }
}

