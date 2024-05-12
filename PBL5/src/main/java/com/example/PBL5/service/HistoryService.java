package com.example.PBL5.service;

import com.example.PBL5.dto.HistoryDTO;
import com.example.PBL5.model.History;
import com.example.PBL5.dto.HistoryResponse;
import com.example.PBL5.repository.IHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoryService implements IHistoryService {
    @Autowired
    private IHistoryRepository historyRepository;

    @Override
    public List<History> findAll() {
        return historyRepository.findAll();
    }

    @Override
    public Page<History> getAllHistory(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        return historyRepository.findAll(pageable);
    }

    @Override
    public List<History> checkNewImage() {
        return historyRepository.checkNewImage();
    }

    @Override
    public void setAllHistoryShowed() {
        historyRepository.setAllHistoryShowed();
    }

    @Override
    public HistoryResponse findAllPage(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<History> histories = historyRepository.findAll(pageable);
        List<History> historyList = histories.getContent();
        List<HistoryDTO> content = historyList.stream().map(p -> mapToDto(p)).collect(Collectors.toList());

        HistoryResponse historyResponse = new HistoryResponse();
//        historyResponse.setContent(content);
//        historyResponse.setPage(histories.getNumber());
//        historyResponse.setPageSize(histories.getSize());
//        historyResponse.setTotalElements(histories.getTotalElements());
//        historyResponse.setTotalPages(histories.getTotalPages());
//        historyResponse.setLast(histories.isLast());

        return historyResponse;
    }

    private HistoryDTO mapToDto(History history) {
        HistoryDTO history1 = new HistoryDTO();
//        history1.setId(history.getId());
//        history1.setImage(history.getImage());
//        history1.setTime(history.getTime());
//        history1.setAmount(history.getAmount());
//        history1.setShowed(history.isShowed());
        return history1;
    }

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
