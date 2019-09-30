package com.autodoc.pfa.pfaautodoc.controllers;

import com.autodoc.pfa.pfaautodoc.models.IndividualFileSubstitution;
import com.autodoc.pfa.pfaautodoc.services.IDocumentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

@RestController
@Api(description = "Контроллер для работы с документами")
@RequestMapping("/documents")
public class DocumentController {

    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);

    final IDocumentService iDocumentService;

    @Autowired
    public DocumentController(IDocumentService iDocumentService) {
        this.iDocumentService = iDocumentService;
    }

    @ApiOperation(value = "Производит генерацию документов для физического лица")
    @PostMapping(value = "/individual/")
    @ResponseBody
    void generateDocumentsForIndividual(HttpServletRequest request,
                                        HttpServletResponse response,
                                        @ApiParam(value = "Данные замены") IndividualFileSubstitution dataToChange) {

    }
}
