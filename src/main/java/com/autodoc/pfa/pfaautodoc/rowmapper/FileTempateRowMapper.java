package com.autodoc.pfa.pfaautodoc.rowmapper;

import com.autodoc.pfa.pfaautodoc.models.FileTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FileTempateRowMapper implements RowMapper<FileTemplate> {
    @Override
    public FileTemplate mapRow(ResultSet resultSet, int i) throws SQLException {
        FileTemplate fileTemplate = new FileTemplate();

        fileTemplate.setId(resultSet.getInt("id"));
        fileTemplate.setName(resultSet.getString("name"));
        fileTemplate.setType(resultSet.getString("type"));
        fileTemplate.setPath(resultSet.getString("path"));

        return fileTemplate;
    }
}
