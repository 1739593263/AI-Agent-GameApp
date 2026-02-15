package com.springai.polymodalaiagent.tools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.IOException;

public class WebScrapingTool {
    @Tool(description = "Scraping a webpage")
    public String scrapWebPage(@ToolParam(description = "The url of webpage to scrap") String url) {
        try {
            Document document = Jsoup.connect(url).get();
            return document.html();
        } catch (Exception e) {
            return "Error scraping webpage: "+e.getMessage();
        }
    }
}
