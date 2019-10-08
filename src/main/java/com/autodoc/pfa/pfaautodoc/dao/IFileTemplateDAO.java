package com.autodoc.pfa.pfaautodoc.dao;

import com.autodoc.pfa.pfaautodoc.models.FileTemplate;

import java.util.List;

public interface IFileTemplateDAO {
    List<FileTemplate> getFileTemplates(String fileType, Boolean isSigned);
}
