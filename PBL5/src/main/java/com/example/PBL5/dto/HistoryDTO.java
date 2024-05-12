package com.example.PBL5.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistoryDTO {
    private int id;
    private String image;
    private String time;
    private int amount;
    private boolean showed;
}
