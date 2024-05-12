package com.example.PBL5.dto;
import com.example.PBL5.dto.HistoryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoryResponse {
    private List<HistoryDTO> content;
    private int page;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
