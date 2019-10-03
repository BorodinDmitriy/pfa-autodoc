package com.autodoc.pfa.pfaautodoc.services;

import com.autodoc.pfa.pfaautodoc.models.FileSubstitution;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public interface IDocumentService {
    File getProcessedFiles(FileSubstitution fs, String fileType);
    ArrayList<ZipFile> getInitialTemplates(String fileType);
}
