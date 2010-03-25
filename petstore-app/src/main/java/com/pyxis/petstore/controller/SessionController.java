package com.pyxis.petstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
public class SessionController {

    @RequestMapping(value = "/logout", method = RequestMethod.DELETE)
    public String delete(HttpSession session) {
        session.invalidate();
        return "redirect:/home";
    }
}
