package com.example.ShereBox.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ShereBox.entity.Lending;

public interface LendingRepository extends JpaRepository<Lending, Long> {
    // マイページ用：自分が借りているもの一覧を取得
    List<Lending> findByBorrowerName(String borrowerName);
}