package com.autodoc.pfa.pfaautodoc.dao.impl;

import com.autodoc.pfa.pfaautodoc.dao.IFileTemplateDAO;
import com.autodoc.pfa.pfaautodoc.models.FileTemplate;
import com.autodoc.pfa.pfaautodoc.rowmapper.FileTempateRowMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class FileTemplateDAOImpl implements IFileTemplateDAO {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static RowMapper<FileTemplate> fileTemplateRowMapper = new FileTempateRowMapper();

    public FileTemplateDAOImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<FileTemplate> getFileTemplates(String fileType, Boolean isSigned) {
        final String sql = "SELECT f.* FROM files AS f WHERE type=:fileType AND is_signed=:isSigned";
        SqlParameterSource namedParameters = new MapSqlParameterSource("fileType", fileType);
        ((MapSqlParameterSource) namedParameters).addValue("isSigned",isSigned);
        List<FileTemplate> fileTemplates = null;
        try {

            fileTemplates = jdbcTemplate.query(sql, namedParameters, fileTemplateRowMapper);
        } catch (DataAccessException ex) {

        }
        return fileTemplates;
    }
}
