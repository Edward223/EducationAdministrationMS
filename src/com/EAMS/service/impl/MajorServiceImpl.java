package com.EAMS.service.impl;


import com.EAMS.dao.MajorDao;
import com.EAMS.dao.impl.MajorDaoImpl;
import com.EAMS.domain.Major;
import com.EAMS.service.MajorService;
import com.EAMS.util.JdbcUtil;

import java.sql.Connection;
import java.util.List;

public class MajorServiceImpl implements MajorService {
    Connection connection = JdbcUtil.getCon();
    MajorDao majorDao = new MajorDaoImpl(connection);


    @Override
    public List<Major> getMajorListByIid(String iid) {
        return majorDao.getMajorListByIid(iid);
    }

    @Override
    public String getMajorIdByName(String name) {
        return majorDao.getMajorIdByName(name);
    }

    @Override
    public List<Major> getMajorListByIname(String iName) {
        return majorDao.getMajorListByIname(iName);
    }
}
