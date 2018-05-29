package com.rubicon.crypto.publisher.web;

import com.rubicon.crypto.publisher.service.AdService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AdController {

    @Autowired
    private AdService adService;

    @GetMapping("/")
    public ModelAndView getAd() {

        ModelAndView modelAndView = new ModelAndView("advertise");
        modelAndView.addObject("content", adService.getContent());

        return modelAndView;
    }
}
