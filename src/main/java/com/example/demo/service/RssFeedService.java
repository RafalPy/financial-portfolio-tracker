package com.example.demo.service;

import com.rometools.rome.feed.synd.SyndFeed;
import com.example.demo.dto.RssFeedItem;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RssFeedService {

    private static final String FEED_URL = "https://feeds.marketwatch.com/marketwatch/topstories/";

    public List<RssFeedItem> getLatestNews() {
        try {
            URL url = new URL(FEED_URL);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(url));

            return feed.getEntries().stream()
                    .map(entry -> new RssFeedItem(
                            entry.getTitle(),
                            entry.getLink(),
                            entry.getDescription() != null ? entry.getDescription().getValue() : "No description"
                    ))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch or parse RSS feed", e);
        }
    }
}

