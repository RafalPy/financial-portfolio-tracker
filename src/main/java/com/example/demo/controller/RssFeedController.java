package com.example.demo.controller;

import com.example.demo.dto.RssFeedItem;
import com.example.demo.service.RssFeedService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news")
public class RssFeedController {

    @Autowired
    private RssFeedService rssFeedService;

    @GetMapping
    public List<RssFeedItem> getFinancialNews() {
        return rssFeedService.getLatestNews();
    }
}