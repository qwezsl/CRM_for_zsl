package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface ClueService {
    List<User> getUserList();

    boolean saveclue(Clue clue);

    Clue getClue(String id);

    List<Activity> Get_Clue_and_activity(String id);

    boolean removeActivity(String id);


    boolean inputRelation(String cid, String[] aid);

    boolean convert(String clueId, Tran tran, String createBy);
}
