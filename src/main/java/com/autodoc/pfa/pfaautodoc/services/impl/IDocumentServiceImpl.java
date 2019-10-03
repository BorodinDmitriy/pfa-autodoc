package com.autodoc.pfa.pfaautodoc.services.impl;

import com.autodoc.pfa.pfaautodoc.dao.IFileTemplateDAO;
import com.autodoc.pfa.pfaautodoc.models.FileSubstitution;
import com.autodoc.pfa.pfaautodoc.models.FileTemplate;
import com.autodoc.pfa.pfaautodoc.services.IDocumentService;
import com.autodoc.pfa.pfaautodoc.utils.FileProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.*;

@Service
@Transactional
public class IDocumentServiceImpl  implements IDocumentService {

    private IFileTemplateDAO iFileTemplateDAO;
    private FileProcessor fileProcessor;

    @Autowired
    public IDocumentServiceImpl(IFileTemplateDAO iFileTemplateDAO) {

        this.iFileTemplateDAO = iFileTemplateDAO;
        this.fileProcessor = new FileProcessor();
    }

    @Override
    public ArrayList<ZipFile> getInitialTemplates(FileSubstitution fs, String fileType) {
        ArrayList<ZipFile> resultList = new ArrayList<>();
        List<FileTemplate> fileTemplatesData = iFileTemplateDAO.getFileTemplates(fileType);
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
                            workingDirectory.mkdir();

                            File templateCopy = new File(workingCopyPath);
                            templateCopy.createNewFile();

                            fileProcessor.copyFile(initialTemplate,templateCopy);
                            resultList.add(new ZipFile(templateCopy));

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
    public File getProcessedFiles(FileSubstitution fs, String fileType) {
        ArrayList<ZipFile> templates = getInitialTemplates(fs, fileType);
        if ((templates != null) && (templates.size() > 0)) {
            String filePath = Paths.get(templates.get(0).getName()).toString();
            String zipFilePath = filePath.substring(0,filePath.lastIndexOf("/")) + "/" + fs.getDealNumber() + ".zip";

            try {
                File resultFile = new File(zipFilePath);
                ZipOutputStream out = new ZipOutputStream(new FileOutputStream(resultFile));
                for (ZipFile zf : templates) {
                    String zipEntryName = zf.getName();
                    String zipEntryPath = zipEntryName.substring(zipEntryName.lastIndexOf("/"),zipEntryName.length());
                    ZipEntry e = new ZipEntry(zipEntryPath);
                    out.putNextEntry(e);

                    // TODO: rewrite this using method copyToFile
                    File fileToCopy = new File(zf.getName());
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
