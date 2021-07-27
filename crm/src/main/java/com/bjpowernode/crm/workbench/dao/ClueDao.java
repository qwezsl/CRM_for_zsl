package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Clue;

public interface ClueDao {


    int saveclue(Clue clue);


    Clue getClue(String id);

    int removeActivity(String id);

    Clue getClueById(String clueId);

    int deleteClue(String clueId);
}
