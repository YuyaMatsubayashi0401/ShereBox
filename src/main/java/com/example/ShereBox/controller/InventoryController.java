package com.example.ShereBox.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.ShereBox.entity.Item;
import com.example.ShereBox.repository.ItemRepository;
import com.example.ShereBox.service.InventoryService;

@Controller
public class InventoryController {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/inventory")
    public String index(@RequestParam(name = "keyword", required = false) String keyword, 
                        @RequestParam(name = "category", required = false) String category, 
                        HttpSession session, 
                        Model model) {
        
        String loginUser = (String) session.getAttribute("userName");
        if (loginUser == null) return "redirect:/login";

        List<Item> items;
        boolean hasKeyword = (keyword != null && !keyword.isBlank());
        boolean hasCategory = (category != null && !category.isBlank());

        if (hasKeyword && hasCategory) {
            items = itemRepository.findByNameContainingAndCategory(keyword, category);
        } else if (hasKeyword) {
            items = itemRepository.findByNameContaining(keyword);
        } else if (hasCategory) {
            items = itemRepository.findByCategory(category);
        } else {
            items = itemRepository.findAll();
        }

        model.addAttribute("items", items);
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedCategory", category);
        return "inventory_list";
    }

    @GetMapping("/inventory/add")
    public String addPage(HttpSession session) {
        if (session.getAttribute("userName") == null) return "redirect:/login";
        return "inventory_add";
    }

    @PostMapping("/inventory/add")
    public String bulkRegister(@RequestParam("rawData") String rawData, 
                               @RequestParam("category") String category, 
                               HttpSession session) {
        String ownerName = (String) session.getAttribute("userName");
        if (ownerName != null && !rawData.isBlank()) {
            inventoryService.bulkRegister(rawData, ownerName, category);
        }
        return "redirect:/inventory";
    }

    @GetMapping("/inventory/edit/{id}")
    public String editPage(@PathVariable("id") Long id, HttpSession session, Model model) {
        String loginUser = (String) session.getAttribute("userName");
        Item item = itemRepository.findById(id).orElseThrow();

        if (loginUser == null || !loginUser.equals(item.getOwnerName())) {
            return "redirect:/inventory";
        }

        model.addAttribute("item", item);
        return "inventory_edit";
    }

    @PostMapping("/inventory/edit")
    public String updateItem(@RequestParam("id") Long id,
                             @RequestParam("name") String name,
                             @RequestParam("totalCount") Integer totalCount,
                             @RequestParam("category") String category,
                             HttpSession session) {
        String loginUser = (String) session.getAttribute("userName");
        Item item = itemRepository.findById(id).orElseThrow();

        if (loginUser != null && loginUser.equals(item.getOwnerName())) {
            item.setName(name);
            item.setTotalCount(totalCount);
            item.setCategory(category);
            itemRepository.save(item);
        }
        return "redirect:/inventory";
    }

    /**
     * アイテムの削除機能（安全なGET方式）
     */
    @GetMapping("/inventory/delete/{id}")
    public String deleteItem(@PathVariable("id") Long id, HttpSession session) {
        String loginUser = (String) session.getAttribute("userName");
        
        itemRepository.findById(id).ifPresent(item -> {
            // 所有者本人の場合のみ削除実行
            if (loginUser != null && loginUser.equals(item.getOwnerName())) {
                itemRepository.delete(item);
            }
        });
        
        return "redirect:/inventory";
    }
}