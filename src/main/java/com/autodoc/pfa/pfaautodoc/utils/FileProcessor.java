package com.autodoc.pfa.pfaautodoc.utils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.docx4j.Docx4J;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.Text;

import javax.xml.bind.JAXBElement;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
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

    public String getSignImageEntryName(String filePath) {
        String result = "";
        long imageSize = 6834;
        try (ZipInputStream zip = new ZipInputStream(new ByteArrayInputStream(Files.readAllBytes(Paths.get(filePath))))) {
            for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
                String name = entry.getName();
                if ((name.contains("media")) && (entry.getSize() == imageSize)) {
                    return name;
                }
            }
        } catch (ZipException ex) {

        } catch (IOException ex) {

        }
        return result;
    }
    public String modifyZipFile(String filePath) {
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
                    byte[] buf;

                    ZipEntry locZE = new ZipEntry(entry.getName());
                    zos.putNextEntry(locZE);
                    if (name.equalsIgnoreCase(getSignImageEntryName(filePath))) {
                        Path fileLocation = Paths.get("/opt/pfaautodoc/images/sign.png");
                        try {
                            buf = Files.readAllBytes(fileLocation);
                        } catch (Exception e) {
                            buf = readByteArray(zip, entrySize);
                        }
                    } else {
                        buf = readByteArray(zip, entrySize);
                    }
                    zos.write(buf, 0, buf.length);
                    zos.closeEntry();
                }
            }
            zos.close();

            return newFilePath;

        } catch (ZipException e) {
            e.printStackTrace();
            return filePath;

        } catch (IOException e) {
            e.printStackTrace();
            return filePath;
        }
    }

    public List<Object> getAllElementFromObject(Object obj, Class<?> toSearch) {
        List<Object> result = new ArrayList<Object>();
        if (obj instanceof JAXBElement)
            obj = ((JAXBElement<?>) obj).getValue();

        if (obj.getClass().equals(toSearch))
            result.add(obj);
        else if (obj instanceof ContentAccessor) {
            List<?> children = ((ContentAccessor) obj).getContent();
            for (Object child : children) {
                result.addAll(getAllElementFromObject(child, toSearch));
            }

        }
        return result;
    }

    public String modifyDocx(String pathToDocx, HashMap<String,String> substitutionData) {
        String newPathToDocx = pathToDocx.substring(0,pathToDocx.lastIndexOf('/') + 1) +
                "_" + pathToDocx.substring(pathToDocx.lastIndexOf('/') + 1);


        try {
            // TODO: templates must be prepared as ${VAR_NAME_FROM_DATABASE} and the whole thing must be inside <w:t></w:t> tags
            WordprocessingMLPackage template = WordprocessingMLPackage
                    .load(new FileInputStream(new File(pathToDocx)));
            List<Object> texts = getAllElementFromObject(
                    template.getMainDocumentPart(), Text.class);

            template.getMainDocumentPart().variableReplace(substitutionData);
            template.save(new java.io.File(newPathToDocx) );
            return newPathToDocx;
        } catch (Exception ex) {
            ex.getMessage();
        }

        return pathToDocx;
    }

    public String modifyPdf(String pathToPdf) {
        String newPathToPdf = pathToPdf.substring(0,pathToPdf.lastIndexOf('/') + 1) +
                "_" + pathToPdf.substring(pathToPdf.lastIndexOf('/') + 1);

        try {
            PdfReader reader = new PdfReader(pathToPdf); // input PDF
            PdfStamper stamper = new PdfStamper(reader,
                    new FileOutputStream(newPathToPdf)); // output PDF

            //loop on pages (1-based)
            for (int i = 1; i <= reader.getNumberOfPages(); i++){

                // get object for writing over the existing content;
                // you can also use getUnderContent for writing in the bottom layer
                PdfContentByte over = stamper.getOverContent(i);

                Rectangle rect = new Rectangle(0,0,10,reader.getPageSize(i).getHeight());
                rect.setBackgroundColor(BaseColor.WHITE);

                over.rectangle(rect);
                over.stroke();
            }

            stamper.close();

            return newPathToPdf;
        } catch (IOException e) {
            return pathToPdf;
        } catch (DocumentException e) {
            return pathToPdf;
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
