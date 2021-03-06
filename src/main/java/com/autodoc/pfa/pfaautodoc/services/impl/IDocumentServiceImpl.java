package com.autodoc.pfa.pfaautodoc.services.impl;

import com.autodoc.pfa.pfaautodoc.dao.IFileTemplateDAO;
import com.autodoc.pfa.pfaautodoc.dao.ISubstitutionDAO;
import com.autodoc.pfa.pfaautodoc.models.FileSubstitution;
import com.autodoc.pfa.pfaautodoc.models.FileTemplate;
import com.autodoc.pfa.pfaautodoc.models.IndividualFileSubstitution;
import com.autodoc.pfa.pfaautodoc.services.IDocumentService;
import com.autodoc.pfa.pfaautodoc.utils.DocxManipulator;
import com.autodoc.pfa.pfaautodoc.utils.FileProcessor;
import com.autodoc.pfa.pfaautodoc.utils.ObjectInspector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.*;

@Service
@Transactional
public class IDocumentServiceImpl  implements IDocumentService {

    private IFileTemplateDAO iFileTemplateDAO;
    private ISubstitutionDAO iSubstitutionDAO;
    private FileProcessor fileProcessor;
    private ObjectInspector objectInspector;

    @Autowired
    public IDocumentServiceImpl(IFileTemplateDAO iFileTemplateDAO,ISubstitutionDAO iSubstitutionDAO) {

        this.iFileTemplateDAO = iFileTemplateDAO;
        this.iSubstitutionDAO = iSubstitutionDAO;
        this.fileProcessor = new FileProcessor();
        this.objectInspector = new ObjectInspector();
    }

    @Override
    public ArrayList<String> getInitialTemplates(FileSubstitution fs, String fileType) {
        ArrayList<String> resultList = new ArrayList<>();
        List<FileTemplate> fileTemplatesData = iFileTemplateDAO.getFileTemplates(fileType,fs.getNeedsStampAndSign());
        if ((fileTemplatesData != null) && (fs.getFilesToModify().size() != 0)) {
            for (FileTemplate ft : fileTemplatesData) {
                for (String checker : fs.getFilesToModify()) {
                    if (ft.getName().contains(checker + "_")) {
                        String path = ft.getPath() + ft.getName();
                        String workingDirectoryPath = ft.getPath() + "processed/";
                        String workingCopyPath = workingDirectoryPath + ft.getName();
                        try {
                            File initialTemplate = new File(path);

                            File workingDirectory = new File(workingDirectoryPath);
                            System.out.println("try mkdir");
                            workingDirectory.mkdir();

                            System.out.println("try createfile");
                            File templateCopy = new File(workingCopyPath);
                            templateCopy.createNewFile();

                            System.out.println("try copyfile");
                            fileProcessor.copyFile(initialTemplate,templateCopy);
                            resultList.add(workingCopyPath);

                            System.out.println("fileaddok");
                        }  catch (Exception ex) {
                            ex.printStackTrace();
                            resultList = null;
                        }
                        break;
                    }
                }

            }
        }
        return resultList;
    }

    @Override
    public void cleanupTempDirectory(String directoryPath) {
        fileProcessor.deleteDirectory(new File(directoryPath));
    }

    @Override
    public HashMap<String, String> getSubstitutionData(FileSubstitution fs, String fileType) {
        HashMap<String,String> resultMap = new HashMap<>();
        List<String> templatesToSubstitute = null;
        List<String> dataFromClientFields = new ArrayList<>();

        templatesToSubstitute = iSubstitutionDAO.getSubstitutionsForFileType(fileType);
        dataFromClientFields.addAll(ObjectInspector.inspect(FileSubstitution.class));
        dataFromClientFields.addAll(ObjectInspector.inspect(fs.getClass()));
        if ((templatesToSubstitute != null) && (dataFromClientFields.size() > 0) ) {
            for (int i = 0; i < templatesToSubstitute.size(); i++) {
                String templateCopy = templatesToSubstitute.get(i);
                templateCopy = templateCopy.replaceAll("[%_]","");
                templateCopy = templateCopy.toLowerCase();
                for (int j = 0; j < dataFromClientFields.size(); j++) {
                    String dataFieldNameCopy = dataFromClientFields.get(j);
                    dataFieldNameCopy = dataFieldNameCopy.toLowerCase();
                    if (templateCopy.equals(dataFieldNameCopy)) {
                        resultMap.put("${" + templatesToSubstitute.get(i) + "}",
                                ObjectInspector.getValueByField(fs, dataFromClientFields.get(j)));
                    }
                }
            }
        }
        return resultMap;
    }

    @Override
    public File getProcessedFiles(FileSubstitution fs, String fileType) {
        DocxManipulator manipulator = new DocxManipulator();
        ArrayList<String> templates = getInitialTemplates(fs, fileType);

        // получение данных о заменяемых шаблонах - что на что заменить
        HashMap<String,String> substitutionMap = getSubstitutionData(fs,fileType);
        fs.setNeedsConversionToPdf(false);
//        if (substitutionMap.containsKey("${%DEAL_NUMBER%}")) {
//            String[] splitDealNumber = substitutionMap.get("${%DEAL_NUMBER%}").split("-");
//            substitutionMap.put("${%DEAL_NUMBER_PTS%}", splitDealNumber[0] + '.' + splitDealNumber[1] + '.' + splitDealNumber[2] + ' ');
//        }

        if (substitutionMap.containsKey("${%CUSTOMER%}")) {
            String[] splitCustomer = substitutionMap.get("${%CUSTOMER%}").split(" ");
            String shortCustomer;
            switch (splitCustomer.length) {
                case 3:
                    shortCustomer = splitCustomer[1].substring(0,1) + "." + splitCustomer[2].substring(0,1) + "."  + splitCustomer[0];
                    break;
                case 2:
                    shortCustomer = splitCustomer[1].substring(0,1) + "." + splitCustomer[0];
                    break;
                default:
                    shortCustomer = substitutionMap.get("${%CUSTOMER%}");
                    break;
            }
            substitutionMap.put("${%CUSTOMER_SHORT%}", shortCustomer);
        }
        if (substitutionMap.containsKey("${%HEAD_CRED%}")) {
            String[] splitCustomer = substitutionMap.get("${%HEAD_CRED%}").split(" ");
            String shortCustomer;
            switch (splitCustomer.length) {
                case 3:
                    shortCustomer = splitCustomer[1].substring(0,1) + "."  + splitCustomer[2].substring(0,1) + "." + splitCustomer[0];
                    break;
                case 2:
                    shortCustomer = splitCustomer[1].substring(0,1) + "." + splitCustomer[0];
                    break;
                default:
                    shortCustomer = substitutionMap.get("${%HEAD_CRED%}");
                    break;
            }
            substitutionMap.put("${%HEAD_CRED_SHORT%}", shortCustomer);
        }

        if (!substitutionMap.containsKey("${%ORG_NAME%}")) {
            substitutionMap.put("${%ORG_NAME%}", "");
        }

        if ((templates != null) && (templates.size() > 0)) {
            ArrayList<String> parsed_templates = new ArrayList<>();
            // замена полученных шаблонов на данные с формы
            for (String zf : templates) {
                if (fs.getNeedsStampAndSign()) {
                    zf = fileProcessor.modifyZipFile(zf); // remove string
                }

                // parsed_templates.add(fileProcessor.modifyDocx(zf, substitutionMap));
                parsed_templates.add(DocxManipulator.generateDocx(zf,substitutionMap));
            }
            templates = parsed_templates;

            // преобразование в *.pdf при необходимости
            if (fs.getNeedsConversionToPdf()) {
                ArrayList<String> pdfs = new ArrayList<>();
                for (String template : templates) {
                    String pdfPath = template.substring(0,template.lastIndexOf(".")) + ".pdf";
                    //fileProcessor.createPDF(template, pdfPath);
                    fileProcessor.convertToPDF(template,pdfPath);
                    pdfPath = fileProcessor.modifyPdf(pdfPath);
                    pdfs.add(pdfPath);
                }
                templates = pdfs;
            }

            // составление конечного архива с документами

            String filePath = templates.get(0);
            String zipFilePath = filePath.substring(0,filePath.lastIndexOf("/")) + "/" + fs.getDealNumber() + ".zip";

            try {
                File resultFile = new File(zipFilePath);
                ZipOutputStream out = new ZipOutputStream(new FileOutputStream(resultFile));
                for (String zf : templates) {
                    String zipEntryName = zf;
                    String zipEntryPath = zipEntryName.substring(zipEntryName.lastIndexOf("/"),zipEntryName.length());
                    ZipEntry e = new ZipEntry(zipEntryPath);
                    out.putNextEntry(e);

                    // TODO: rewrite this using method copyToFile
                    File fileToCopy = new File(zf);
                    FileInputStream fin = new FileInputStream(fileToCopy);
                    byte[] b = new byte[512];
                    int len = 0;
                    while ((len = fin.read(b)) != -1)
                    {
                        out.write(b, 0, len);
                    }

                    out.closeEntry();
                }
                out.close();
                return resultFile;
            } catch (IOException ex) {
                return null;
            }

        }
        return null;
    }
}
