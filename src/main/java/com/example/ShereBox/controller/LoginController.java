package com.example.ShereBox.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    // 1. ログイン画面を表示する
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // templates/login.html を探す
    }

    // 2. 名前が選ばれた時の処理
    @PostMapping("/login")
    public String login(@RequestParam("userName") String userName, HttpSession session) {
        // セッションに名前を保存
        session.setAttribute("userName", userName);
        // 在庫一覧へ飛ばす
        return "redirect:/inventory";
    }

    // 3. ログアウト処理
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // セッションを破棄
        return "redirect:/login";
    }
}