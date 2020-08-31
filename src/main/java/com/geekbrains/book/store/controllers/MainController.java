package com.geekbrains.book.store.controllers;

import com.geekbrains.book.store.beans.Cart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
@AllArgsConstructor
public class MainController {
    private Cart cart;

    @GetMapping
    public String showHomePage() {
        return "about-page";
    }

    @GetMapping("/many")
    @ResponseBody
    public String manyParams(@RequestParam MultiValueMap params) {
        return "1";
    }

    @GetMapping("/principal")
    @ResponseBody
    public String showPrincipal(Principal principal) {
        return "Demo msg";
    }
}
