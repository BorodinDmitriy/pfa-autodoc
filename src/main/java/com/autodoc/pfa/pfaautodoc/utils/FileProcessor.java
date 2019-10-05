package com.autodoc.pfa.pfaautodoc.utils;

import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.docx4j.Docx4J;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.*;

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

    public void readPDF(String pdfPath) {
        try (PDDocument document = PDDocument.load(new File(pdfPath))) {

            document.getClass();

            if (!document.isEncrypted()) {

                PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                stripper.setSortByPosition(true);

                PDFTextStripper tStripper = new PDFTextStripper();

                String pdfFileInText = tStripper.getText(document);
                //System.out.println("Text:" + st);

                // split by whitespace
                String lines[] = pdfFileInText.split("\\n");
                for (String line : lines) {
                    System.out.println(line);
                }

            }

        } catch (Exception ex) {

        }
    }

    public byte[] readByteArray(ZipInputStream zip, int expected) throws IOException {
        if (expected == -1) { // unknown size
            return IOUtils.toByteArray(zip);
        }
        final byte[] bytes = new byte[expected];
        int read = 0;
        do {
            int n = zip.read(bytes, read, expected - read);
            if (n <= 0) {
                break;
            }
            read += n;
        } while (read < expected);

        if (expected != bytes.length) {
            throw new EOFException("Unexpected EOF");
        }
        return bytes;
    }

    public String modifyZipFile(String filePath, HashMap<String,String> substitutionData) {
        try {
            String newFilePath = filePath.substring(0,filePath.lastIndexOf('/') + 1) +
                    "_" + filePath.substring(filePath.lastIndexOf('/') + 1);

            final ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(newFilePath));
            try (ZipInputStream zip = new ZipInputStream(new ByteArrayInputStream(Files.readAllBytes(Paths.get(filePath))))) {
                for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
                    String name = entry.getName();
                    int entrySize = (int) entry.getSize();
                    if (entrySize >= Integer.MAX_VALUE) {
                        throw new OutOfMemoryError("Too large file " + name + ", size is " + entrySize);
                    }
                    byte[] buf = readByteArray(zip, entrySize);
                    if (name.equalsIgnoreCase("word/document.xml")) {
                        String s = new String(buf);
                        for (Map.Entry<String,String> mapEntry : substitutionData.entrySet())
                            s = s.replaceAll(mapEntry.getKey(),mapEntry.getValue());
                        buf = s.getBytes("UTF-8");
                    }
                    ZipEntry locZE = new ZipEntry(entry.getName());
                    zos.putNextEntry(locZE);
                    zos.write(buf, 0, entrySize);
                    zos.closeEntry();
                }
            }
            zos.close();
            /*ZipFile zipFile = new ZipFile(filePath);
            final ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(newFilePath));
            for(Enumeration e = zipFile.entries(); e.hasMoreElements(); ) {
                ZipEntry entryIn = (ZipEntry) e.nextElement();
                if (!entryIn.getName().equalsIgnoreCase("word/document.xml")) {
                    ZipEntry locZE = new ZipEntry(entryIn.getName());
                    zos.putNextEntry(locZE);
                    InputStream is = zipFile.getInputStream(entryIn);
                    byte[] buf = new byte[1024];
                    int len;
                    while((len = is.read(buf)) > 0) {
                        zos.write(buf, 0, (len < buf.length) ? len : buf.length);
                    }
                }
                else{
                    ZipEntry locZE = new ZipEntry(entryIn.getName());
                    zos.putNextEntry(locZE);
                    InputStream is = zipFile.getInputStream(entryIn);
                    byte[] buf = new byte[1024];
                    int len;
                    while((len = is.read(buf)) > 0) {
                        String s = new String(buf);
                        for (Map.Entry<String,String> mapEntry : substitutionData.entrySet())
                            buf = s.replaceAll(mapEntry.getKey(),mapEntry.getValue()).getBytes();
                        zos.write(buf, 0,  (len < buf.length) ? len : buf.length);
                    }
                }
                zos.closeEntry();
            }
            zos.close();*/
            /*ZipFile zip = new ZipFile(new File(filePath));

            for (Enumeration e = zip.entries(); e.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) e.nextElement();
                File fileToBeModified = new File(entry.getName());
                String entryName = fileToBeModified.getName();
                if (entryName.equals("document.xml")) {
                    String oldContent = "";
                    BufferedReader reader = null;
                    FileWriter writer = null;

                    reader = new BufferedReader(new FileReader(fileToBeModified));

                    // Reading all the lines of input text file into oldContent
                    String line = reader.readLine();
                    while (line != null) {
                        oldContent = oldContent + line + System.lineSeparator();
                        line = reader.readLine();
                    }

                    // Replacing oldString with newString in the oldContent
                    String newContent = oldContent;
                    for (Map.Entry<String,String> mapEntry : substitutionData.entrySet())
                        newContent = newContent.replaceAll(mapEntry.getKey(),mapEntry.getValue());

                    // Rewriting the input text file with newContent
                    writer = new FileWriter(fileToBeModified);
                    writer.write(newContent);

                    reader.close();
                    writer.close();
                }
            }*/
            return newFilePath;

        } catch (ZipException e) {
            e.printStackTrace();
            return filePath;

        } catch (IOException e) {
            e.printStackTrace();
            return filePath;
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
