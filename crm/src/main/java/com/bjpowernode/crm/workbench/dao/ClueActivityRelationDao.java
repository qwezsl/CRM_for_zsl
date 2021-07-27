package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;

import java.util.List;
import java.util.Map;

public interface ClueActivityRelationDao {


    int inputRelation(Map<String,String> map);

    List<ClueActivityRelation> getCar(String clueId);

    int deleteClueActivity(String clueId);
}
