package com.example.ShereBox.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;

import lombok.Data;

@Entity
@Data
public class Lending {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 貸し出されているアイテムのID（Itemテーブルとの紐付け）
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    // 借りている人の名前（ログインユーザー名）
    private String borrowerName;

    // 借りている個数
    private Integer borrowedCount;

    // 貸出日時（いつ借りたか分かると便利なので追加）
    private LocalDateTime borrowedAt;

    @PrePersist
    protected void onCreate() {
        this.borrowedAt = LocalDateTime.now();
    }
}