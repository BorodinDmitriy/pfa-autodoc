package com.autodoc.pfa.pfaautodoc.utils;

import java.io.*;
import java.net.URI;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class DocxManipulator {
    private static final String MAIN_DOCUMENT_PATH = "word/document.xml";
    private static final String TEMPLATE_DIRECTORY_ROOT = "TEMPLATES_DIRECTORY/";


    /*    PUBLIC METHODS    */

    /**
     * Generates .docx document from given template and the substitution data
     *
     * @param pathToDocx
     *            Template data path
     * @param substitutionData
     *            Hash map with the set of key-value pairs that represent
     *            substitution data
     * @return
     */
    public static String generateDocx(String pathToDocx, Map<String,String> substitutionData) {

        String newPathToDocx = pathToDocx.substring(0,pathToDocx.lastIndexOf('/') + 1) +
                "_" + pathToDocx.substring(pathToDocx.lastIndexOf('/') + 1);

        String userTempDir = UUID.randomUUID().toString();
        userTempDir = TEMPLATE_DIRECTORY_ROOT + userTempDir + "/";

        try {

            // Unzip .docx file
            unzip(new File(pathToDocx), new File(userTempDir));

            // Change data
            changeData(new File(userTempDir + MAIN_DOCUMENT_PATH), substitutionData);

            // Rezip .docx file
            zip(new File(userTempDir), new File(newPathToDocx));

            // Clean temp data
            deleteTempData(new File(userTempDir));
        }
        catch (IOException ioe) {
            System.out.println(ioe.getMessage());
            return pathToDocx;
        }

        return newPathToDocx;
    }


    /*    PRIVATE METHODS    */

    /**
     * Unzipps specified ZIP file to specified directory
     *
     * @param zipfile
     *            Source ZIP file
     * @param directory
     *            Destination directory
     * @throws IOException
     */
    private static void unzip(File zipfile, File directory) throws IOException {

        ZipFile zfile = new ZipFile(zipfile);
        Enumeration<? extends ZipEntry> entries = zfile.entries();

        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            File file = new File(directory, entry.getName());
            if (entry.isDirectory()) {
                file.mkdirs();
            }
            else {
                file.getParentFile().mkdirs();
                InputStream in = zfile.getInputStream(entry);
                try {
                    copy(in, file);
                }
                finally {
                    in.close();
                }
            }
        }
    }


    /**
     * Substitutes keys found in target file with corresponding data
     *
     * @param targetFile
     *            Target file
     * @param substitutionData
     *            Map of key-value pairs of data
     * @throws IOException
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static void changeData(File targetFile, Map<String,String> substitutionData) throws IOException{

        BufferedReader br = null;
        String docxTemplate = "";
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(targetFile), "UTF-8"));
            String temp;
            while( (temp = br.readLine()) != null)
                docxTemplate = docxTemplate + temp;
            br.close();
            targetFile.delete();
        }
        catch (IOException e) {
            br.close();
            throw e;
        }

        Iterator substitutionDataIterator = substitutionData.entrySet().iterator();
        while(substitutionDataIterator.hasNext()){
            Map.Entry<String,String> pair = (Map.Entry<String,String>)substitutionDataIterator.next();
            if(docxTemplate.contains(pair.getKey())){
                if(pair.getValue() != null)
                    docxTemplate = docxTemplate.replace(pair.getKey(), pair.getValue());
                else
                    docxTemplate = docxTemplate.replace(pair.getKey(), "NEDOSTAJE");
            }
        }

        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(targetFile);
            fos.write(docxTemplate.getBytes("UTF-8"));
            fos.close();
        }
        catch (IOException e) {
            fos.close();
            throw e;
        }
    }

    /**
     * Zipps specified directory and all its subdirectories
     *
     * @param directory
     *            Specified directory
     * @param zipfile
     *            Output ZIP file name
     * @throws IOException
     */
    private static void zip(File directory, File zipfile) throws IOException {

        URI base = directory.toURI();
        Deque<File> queue = new LinkedList<File>();
        queue.push(directory);
        OutputStream out = new FileOutputStream(zipfile);
        Closeable res = out;

        try {
            ZipOutputStream zout = new ZipOutputStream(out);
            res = zout;
            while (!queue.isEmpty()) {
                directory = queue.pop();
                for (File kid : directory.listFiles()) {
                    String name = base.relativize(kid.toURI()).getPath();
                    if (kid.isDirectory()) {
                        queue.push(kid);
                        name = name.endsWith("/") ? name : name + "/";
                        zout.putNextEntry(new ZipEntry(name));
                    }
                    else {
                        if(kid.getName().contains(".docx"))
                            continue;
                        zout.putNextEntry(new ZipEntry(name));
                        copy(kid, zout);
                        zout.closeEntry();
                    }
                }
            }
        }
        finally {
            res.close();
        }
    }

    /**
     * Deletes directory and all its subdirectories
     *
     * @param file
     *            Specified directory
     * @throws IOException
     */
    public static void deleteTempData(File file) throws IOException {

        System.out.println("DOCXManipulator: removing file or directory : " + file.getName());
        if (file.isDirectory()) {

            // directory is empty, then delete it
            if (file.list().length == 0)
                file.delete();
            else {
                // list all the directory contents
                String files[] = file.list();

                for (String temp : files) {
                    // construct the file structure
                    File fileDelete = new File(file, temp);
                    // recursive delete
                    deleteTempData(fileDelete);
                }

                // check the directory again, if empty then delete it
                if (file.list().length == 0)
                    file.delete();
            }
        } else {
            // if file, then delete it
            file.delete();
        }
    }

    private static void copy(InputStream in, OutputStream out) throws IOException {

        byte[] buffer = new byte[1024];
        while (true) {
            int readCount = in.read(buffer);
            if (readCount < 0) {
                break;
            }
            out.write(buffer, 0, readCount);
        }
    }

    private static void copy(File file, OutputStream out) throws IOException {
        InputStream in = new FileInputStream(file);
        try {
            copy(in, out);
        } finally {
            in.close();
        }
    }

    private static void copy(InputStream in, File file) throws IOException {
        OutputStream out = new FileOutputStream(file);
        try {
            copy(in, out);
        } finally {
            out.close();
        }
    }
}
