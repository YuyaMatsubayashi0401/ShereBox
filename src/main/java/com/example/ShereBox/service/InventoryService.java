package com.example.ShereBox.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ShereBox.entity.Item;
import com.example.ShereBox.repository.ItemRepository;

@Service
public class InventoryService {

    @Autowired
    private ItemRepository itemRepository;

    @Transactional
    public void bulkRegister(String rawData, String ownerName, String category) {
        String[] lines = rawData.split("\\r?\\n");

        for (String line : lines) {
            if (line.isBlank()) continue;

            // 分割ルール: 「,」「、」「・」「/」
            String[] parts = line.split("[,、・/:;：；]");
            
            Item item = new Item();
            item.setName(parts[0].trim());
            item.setOwnerName(ownerName);
            item.setCategory(category);

            // 枚数判定: 2つ目の要素がない、または空なら「1」をデフォルト設定
            if (parts.length >= 2 && !parts[1].trim().isEmpty()) {
                try {
                    item.setTotalCount(Integer.parseInt(parts[1].trim()));
                } catch (NumberFormatException e) {
                    item.setTotalCount(1);
                }
            } else {
                item.setTotalCount(1);
            }
            
            itemRepository.save(item);
        }
    }
}