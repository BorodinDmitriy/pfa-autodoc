package com.autodoc.pfa.pfaautodoc.controllers;

import com.autodoc.pfa.pfaautodoc.models.IndividualFileSubstitution;
import com.autodoc.pfa.pfaautodoc.services.IDocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/")
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);


    @Autowired
    public MainController() { }

    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String mainPage(HttpServletRequest request, Model model) {
        return "index";
    }

    @RequestMapping(value = "/individual/",method = RequestMethod.GET)
    public String individualPage(HttpServletRequest request, Model model) {
        model.addAttribute("individualfilesubstitution", new IndividualFileSubstitution());
        return "individual";
    }
}
