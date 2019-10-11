package com.autodoc.pfa.pfaautodoc.controllers;

import com.autodoc.pfa.pfaautodoc.models.IndividualFileSubstitution;
import com.autodoc.pfa.pfaautodoc.models.LegalFileSubstitution;
import com.autodoc.pfa.pfaautodoc.services.IDocumentService;
import com.autodoc.pfa.pfaautodoc.utils.ObjectInspector;
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
import java.util.List;

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
        List<String> substituteFields = ObjectInspector.inspect(IndividualFileSubstitution.class);
        String customer = ObjectInspector.getValueByField(dataToChange, "customer");
        System.out.println("Done");

        File fileToDeliver = iDocumentService.getProcessedFiles(dataToChange, "individual");
        String tempPath = fileToDeliver.toPath().toString();
        String tempDirectory = tempPath.substring(0,tempPath.lastIndexOf('/'));
        if ((fileToDeliver != null) && (Files.exists(fileToDeliver.toPath())))
        {
            response.addHeader("Content-Disposition", "attachment; filename=" + fileToDeliver.getName());
            try
            {
                Files.copy(fileToDeliver.toPath(), response.getOutputStream());
                iDocumentService.cleanupTempDirectory(tempDirectory);
                response.getOutputStream().flush();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        /* String filePath = "./src/main/resources/documents/";

        Path file = Paths.get(filePath, "2.docx");

        try {
            ZipFile zip = new ZipFile(new File(filePath + "2.docx"));

            for (Enumeration e = zip.entries(); e.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) e.nextElement();
                //Here I need to get the simple name instead the full name with its root
                System.out.println(new File(entry.getName()).getName());
            }

        } catch (ZipException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        }


        if (Files.exists(file))
        {
            response.addHeader("Content-Disposition", "attachment; filename=2.docx");
            try
            {
                Files.copy(file, response.getOutputStream());
                response.getOutputStream().flush();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }*/

        //return Paths.get("").toAbsolutePath().toString();
    }
    @ApiOperation(value = "Производит генерацию документов для физического лица")
    @PostMapping(value = "/legal/")
    @ResponseBody
    void generateDocumentsForLegal(HttpServletRequest request,
                                        HttpServletResponse response,
                                        @ApiParam(value = "Данные замены") LegalFileSubstitution dataToChange) {
        File fileToDeliver = iDocumentService.getProcessedFiles(dataToChange, "legal");
        String tempPath = fileToDeliver.toPath().toString();
        String tempDirectory = tempPath.substring(0,tempPath.lastIndexOf('/'));
        if ((fileToDeliver != null) && (Files.exists(fileToDeliver.toPath())))
        {
            response.addHeader("Content-Disposition", "attachment; filename=" + fileToDeliver.getName());
            try
            {
                Files.copy(fileToDeliver.toPath(), response.getOutputStream());
                iDocumentService.cleanupTempDirectory(tempDirectory);
                response.getOutputStream().flush();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
