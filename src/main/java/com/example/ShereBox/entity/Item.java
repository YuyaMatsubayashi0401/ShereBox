package com.example.ShereBox.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;           // アイテム名
    private Integer totalCount;    // 総数
    private String ownerName;      // 所有者
    private String category;       // カテゴリ（追加）

    @Column(length = 100)
    private String searchName;     // 検索用（必要に応じて使用）
}