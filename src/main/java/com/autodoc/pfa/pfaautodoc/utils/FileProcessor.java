package com.autodoc.pfa.pfaautodoc.utils;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.docx4j.Docx4J;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.io.*;

public class FileProcessor {

    public final static int BUF_SIZE = 1024;

    public FileProcessor() {

    }

    public void copyFile(File in, File out) throws Exception {
        FileInputStream fis  = new FileInputStream(in);
        FileOutputStream fos = new FileOutputStream(out);
        try {
            byte[] buf = new byte[BUF_SIZE];
            int i = 0;
            while ((i = fis.read(buf)) != -1) {
                fos.write(buf, 0, i);
            }
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            if (fis != null) fis.close();
            if (fos != null) fos.close();
        }
    }

    public void convertToPDF(String docPath, String pdfPath) {
        try {
            InputStream is = new FileInputStream(
                    new File(docPath));
            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage
                    .load(is);
            OutputStream out = new FileOutputStream(new File(pdfPath));
            Docx4J.toPDF(wordMLPackage, out);
            System.out.println("Done");
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }


    }


    public boolean deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDirectory(children[i]);
                if (!success) {
                    return false;
                }
            }
        }

        // either file or an empty directory
        System.out.println("removing file or directory : " + dir.getName());
        return dir.delete();
    }
}
