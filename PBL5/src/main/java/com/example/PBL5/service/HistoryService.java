package com.example.PBL5.service;

import com.example.PBL5.model.History;
import com.example.PBL5.repository.IHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryService implements IHistoryService {
    @Autowired
    private IHistoryRepository historyRepository;

    @Override
    public List<History> findAll() {
        return historyRepository.findAll();
    }

    @Override
    public void setAllHistoryShowed() {
        historyRepository.setAllHistoryShowed();
    }

//    @Override
//    public Page<History> findAllPage() {
//        return historyRepository.findAll(PageRequest.of(0, 5, Sort.by("time").descending()));
//    }

    @Override
    public History findById(int id) {
        return historyRepository.findById(id).orElse(null);
    }

    @Override
    public History save(History history) {
        return historyRepository.save(history);
    }

    @Override
    public void remove(int id) {
        historyRepository.deleteById(id);
    }

    @Override
    public void remove(History history) {
        historyRepository.delete(history);
    }
}
