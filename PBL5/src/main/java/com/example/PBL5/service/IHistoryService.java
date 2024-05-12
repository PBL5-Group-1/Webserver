package com.example.PBL5.service;

import com.example.PBL5.model.History;
import com.example.PBL5.dto.HistoryResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IHistoryService {
    List<History> findAll();

    Page<History> getAllHistory(int page, int pageSize);
    HistoryResponse findAllPage(int page, int pageSize);

    List<History> checkNewImage();

    void setAllHistoryShowed();

    History findById(int id);

    History save(History history);

    void remove(int id);

    void remove(History history);
}
