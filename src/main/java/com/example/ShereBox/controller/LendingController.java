package com.example.ShereBox.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.ShereBox.service.LendingService;

@Controller
public class LendingController {

    @Autowired
    private LendingService lendingService;

    @PostMapping("/lending/exec")
    public String executeLending(
            @RequestParam(value = "itemIds", required = false) List<Long> itemIds,
            @RequestParam(value = "counts", required = false) List<Integer> counts,
            HttpSession session) {

        String borrowerName = (String) session.getAttribute("userName");

        if (borrowerName != null && itemIds != null && !itemIds.isEmpty()) {
            // サービスを呼び出して貸出を実行
            lendingService.executeLending(itemIds, counts, borrowerName);
        }

        // 完了後はマイページ（借りているもの一覧）へ飛ばす
        return "redirect:/mypage";
    }
    
 // LendingController 内に追加してください
    @Autowired
    private com.example.ShereBox.repository.LendingRepository lendingRepository;

    @org.springframework.web.bind.annotation.GetMapping("/mypage")
    public String myPage(HttpSession session, org.springframework.ui.Model model) {
        String loginUser = (String) session.getAttribute("userName");
        if (loginUser == null) {
            return "redirect:/login";
        }

        // 自分が借りているリストを取得
        List<com.example.ShereBox.entity.Lending> myLendings = lendingRepository.findByBorrowerName(loginUser);
        model.addAttribute("myLendings", myLendings);
        
        return "mypage";
    }
    
 // LendingController.java 内に追記

    @PostMapping("/lending/return")
    public String executeReturn(@RequestParam(value = "lendingIds", required = false) List<Long> lendingIds) {
        if (lendingIds != null && !lendingIds.isEmpty()) {
            lendingService.executeReturn(lendingIds);
        }
        return "redirect:/mypage";
    }
}