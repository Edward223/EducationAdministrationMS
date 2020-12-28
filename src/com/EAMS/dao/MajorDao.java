package com.EAMS.dao;

import com.EAMS.domain.Major;

import java.util.List;

public interface MajorDao {
    List<Major> getMajorListByIid(String iid);

    String getMajorIdByName(String inMajor);

    List<Major> getMajorListByIname(String iName);
}
