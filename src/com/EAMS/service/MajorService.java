package com.EAMS.service;

import com.EAMS.domain.Major;

import java.util.List;

public interface MajorService {
    List<Major> getMajorListByIid(String id);

    String getMajorIdByName(String inMajor);

    List<Major> getMajorListByIname(String toString);
}
