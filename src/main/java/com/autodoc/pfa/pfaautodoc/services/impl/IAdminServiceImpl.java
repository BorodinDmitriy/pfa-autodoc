package com.autodoc.pfa.pfaautodoc.services.impl;

import com.autodoc.pfa.pfaautodoc.services.IAdminService;
import com.autodoc.pfa.pfaautodoc.utils.FileProcessor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Transactional
public class IAdminServiceImpl implements IAdminService {
    private static String uploadDirectory = "./src/main/resources/static/images/";
    private static String signFilename = "sign.png";
    private final FileProcessor fp = new FileProcessor();

    @Override
    public Boolean setImageSign(MultipartFile imageFile) {
        try {
            if (imageFile.isEmpty()) {
                return false;
            }

            File oldSign = new File(uploadDirectory + signFilename);
            if (oldSign.exists()) {
                fp.deleteDirectory(oldSign);
            }

            byte[] bytes = imageFile.getBytes();
            System.out.println(IAdminServiceImpl.class.getResource("").getPath().toString());
            Path path = Paths.get(uploadDirectory + signFilename);
            Files.write(path, bytes);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
