package com.autodoc.pfa.pfaautodoc.controllers;

import com.autodoc.pfa.pfaautodoc.models.AdminSettings;
import com.autodoc.pfa.pfaautodoc.models.IndividualFileSubstitution;
import com.autodoc.pfa.pfaautodoc.services.IAdminService;
import com.autodoc.pfa.pfaautodoc.utils.ObjectInspector;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@RestController
@Api(description = "Контроллер для настроек")
@RequestMapping("/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    private static String briefUploadDirectory = "/static/images/";
    private long limitFileSize = 30000;

    final IAdminService iAdminService;

    @Autowired
    public AdminController(IAdminService iAdminService) {
        this.iAdminService = iAdminService;
    }

    @ApiOperation(value = "Производит настройку работы системы генерации документации")
    @RequestMapping(value = "/update_settings/",method = RequestMethod.POST)
    public ResponseEntity<?> uploadSignImage(@ModelAttribute("adminSettings") AdminSettings adminSettings, @RequestParam(value = "file") MultipartFile image) {

        logger.debug("Single file upload!");

        if (image.isEmpty()) {
            return new ResponseEntity("Please select a file!", HttpStatus.BAD_REQUEST);
        }

        if (image.getSize() > limitFileSize) {
            return new ResponseEntity("Image must be no more than 30000 bytes (300*100 pixels). Please, upload new image.", HttpStatus.BAD_REQUEST);
        }

        if (!image.getOriginalFilename().substring(image.getOriginalFilename().indexOf('.') + 1).equals("png")) {
            return new ResponseEntity("Please select a file of *.png extension", HttpStatus.BAD_REQUEST);
        }

        try {
            if (iAdminService.setImageSign(image)) {
                return new ResponseEntity(briefUploadDirectory + "sign.png", new HttpHeaders(), HttpStatus.OK);
            } else {
                return new ResponseEntity("Couldnt upload file", new HttpHeaders(), HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
