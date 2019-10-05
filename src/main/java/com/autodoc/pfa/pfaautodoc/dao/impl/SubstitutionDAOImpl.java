package com.autodoc.pfa.pfaautodoc.dao.impl;

import com.autodoc.pfa.pfaautodoc.dao.ISubstitutionDAO;
import com.autodoc.pfa.pfaautodoc.models.FileSubstitution;
import com.autodoc.pfa.pfaautodoc.models.FileTemplate;
import com.autodoc.pfa.pfaautodoc.rowmapper.FileSubstitutionTemplatesRowMapper;
import com.autodoc.pfa.pfaautodoc.rowmapper.FileTempateRowMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class SubstitutionDAOImpl implements ISubstitutionDAO {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static RowMapper<String> fileSubstitutionTemplatesRowMapper = new FileSubstitutionTemplatesRowMapper();

    public SubstitutionDAOImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<String> getSubstitutionsForFileType(String fileType) {
        final String sql = "SELECT s.name AS substitute_template FROM substitutes s WHERE s.type='complex' OR s.type=:fileType;";
        SqlParameterSource namedParameters = new MapSqlParameterSource("fileType", fileType);
        List<String> resultList = null;
        try {
            resultList = jdbcTemplate.query(sql, namedParameters, fileSubstitutionTemplatesRowMapper);
        } catch (DataAccessException ex) {

        }
        return resultList;
    }
}
