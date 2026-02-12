package com.example.ShereBox.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ShereBox.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
    
    // 1. 名前だけで検索（従来通り）
    List<Item> findByNameContaining(String name);

    // 2. カテゴリだけで検索（追加：フィルタ機能用）
    List<Item> findByCategory(String category);

    // 3. 名前とカテゴリの両方で絞り込み（追加：フィルタ機能用）
    List<Item> findByNameContainingAndCategory(String name, String category);
}