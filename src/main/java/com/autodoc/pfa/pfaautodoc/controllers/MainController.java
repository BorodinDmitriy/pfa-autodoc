package com.autodoc.pfa.pfaautodoc.controllers;

import com.autodoc.pfa.pfaautodoc.models.AdminSettings;
import com.autodoc.pfa.pfaautodoc.models.IndividualFileSubstitution;
import com.autodoc.pfa.pfaautodoc.models.LegalFileSubstitution;
import com.autodoc.pfa.pfaautodoc.services.IDocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.sql.Timestamp;

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
    @RequestMapping(value = "/legal/",method = RequestMethod.GET)
    public String legalPage(HttpServletRequest request, Model model) {
        model.addAttribute("legalfilesubstitution", new LegalFileSubstitution());
        return "legal";
    }
    @RequestMapping(value = "/admin/",method = RequestMethod.GET)
    public String adminPage(HttpServletRequest request, Model model) {
        String signImage = "./src/main/resources/static/images/sign.png";
        String briefSignImage = "/static/images/sign.png?";
        Boolean successfulUpload = new Boolean(false);
        try {
            File signImageFile = new File(signImage);
            if (!signImageFile.exists()) {
                signImage = "/static/images/nosign.png?";
            } else {
                signImage = briefSignImage;
            }
        } catch (Exception ex) {
            signImage = "/static/images/nosign.png?";
        }

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        signImage = signImage + "timestamp=" + timestamp.getTime();
        model.addAttribute("signPath", signImage);
        model.addAttribute("adminSettings", new AdminSettings());
        return "admin";
    }
}
