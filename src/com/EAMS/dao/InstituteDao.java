package com.EAMS.dao;

import com.EAMS.domain.Institute;

import java.util.List;

public interface InstituteDao {

    List<Institute> getInstituteList();

    String getInstituteIdByName(String name);
}
