package com.autodoc.pfa.pfaautodoc.dao;

import com.autodoc.pfa.pfaautodoc.models.FileSubstitution;

import java.util.List;

public interface ISubstitutionDAO {
    List<String> getSubstitutionsForFileType(String fileType);
}
