package com.example.PBL5.service;

import com.example.PBL5.model.History;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IHistoryService {
    List<History> findAll();

//    public Page<History> findAllPage();
    void setAllHistoryShowed();
    History findById(int id);

    History save(History history);

    void remove(int id);

    void remove(History history);
}
