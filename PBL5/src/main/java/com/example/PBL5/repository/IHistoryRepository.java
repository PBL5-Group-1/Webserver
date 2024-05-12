package com.example.PBL5.repository;

import com.example.PBL5.model.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface IHistoryRepository extends JpaRepository<History, Integer> {
//    @Modifying
//    @Query(value = "SELECT * FROM `history` WHERE STR_TO_DATE(`time`, '%Y-%m-%d %H:%i:%s') BETWEEN ?1 AND  ?2", nativeQuery = true)
    Page<History> findAll(Pageable pageable);

    @Modifying
    @Query(value = "SELECT * FROM `history` WHERE `showed` = 0", nativeQuery = true)
    List<History> checkNewImage();

    @Modifying
    @Transactional
    @Query(value = "UPDATE `history` SET `showed` = 1 WHERE `showed` = 0", nativeQuery = true)
    void setAllHistoryShowed();
}
