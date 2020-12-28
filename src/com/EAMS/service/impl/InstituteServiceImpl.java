package com.EAMS.service.impl;

import com.EAMS.dao.impl.InstituteDaoImpl;
import com.EAMS.domain.Institute;
import com.EAMS.service.InstituteService;
import com.EAMS.util.JdbcUtil;

import java.sql.Connection;
import java.util.List;

public class InstituteServiceImpl implements InstituteService {
    Connection connection = JdbcUtil.getCon();
    InstituteDaoImpl instituteDao = new InstituteDaoImpl(connection);

    @Override
    public List<Institute> getInstituteList() {
        return instituteDao.getInstituteList();
    }

    @Override
    public String getInstituteIdByName(String name) {
        return instituteDao.getInstituteIdByName(name);
    }
}
