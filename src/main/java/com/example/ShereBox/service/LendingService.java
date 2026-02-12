package com.example.ShereBox.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ShereBox.entity.Item;
import com.example.ShereBox.entity.Lending;
import com.example.ShereBox.repository.ItemRepository;
import com.example.ShereBox.repository.LendingRepository;

@Service
public class LendingService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private LendingRepository lendingRepository;

    @Transactional
    public void executeLending(List<Long> itemIds, List<Integer> counts, String borrowerName) {
        for (int i = 0; i < itemIds.size(); i++) {
            Long itemId = itemIds.get(i);
            Integer borrowCount = counts.get(i);

            // 1. アイテムを取得
            Item item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new RuntimeException("アイテムが見つかりません ID: " + itemId));

            // 2. 在庫チェック（足りない場合はスキップまたはエラー）
            if (item.getTotalCount() < borrowCount) {
                continue; // 今回は簡易的にスキップ
            }

            // 3. 在庫数を減らす
            item.setTotalCount(item.getTotalCount() - borrowCount);
            itemRepository.save(item);

            // 4. 貸出記録を作成
            Lending lending = new Lending();
            lending.setItem(item);
            lending.setBorrowerName(borrowerName);
            lending.setBorrowedCount(borrowCount);
            lendingRepository.save(lending);
        }
    }
 // LendingService.java 内に追記

    @Transactional
    public void executeReturn(List<Long> lendingIds) {
        for (Long id : lendingIds) {
            // 貸出記録を取得
            Lending lending = lendingRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("貸出記録が見つかりません"));

            // 1. 在庫を戻す
            Item item = lending.getItem();
            item.setTotalCount(item.getTotalCount() + lending.getBorrowedCount());
            itemRepository.save(item);

            // 2. 貸出記録を削除する
            lendingRepository.delete(lending);
        }
    }
    
}