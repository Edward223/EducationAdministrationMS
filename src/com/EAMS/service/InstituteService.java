package com.EAMS.service;

import com.EAMS.domain.Institute;

import java.util.List;

public interface InstituteService {
    List<Institute> getInstituteList();

    String getInstituteIdByName(String selectInstitute);
}
