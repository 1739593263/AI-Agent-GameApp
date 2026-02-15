package com.springai.polymodalaiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WebScrapingToolTest {

    @Test
    void scrapWebPage() {
        String url = "https://github.com/1739593263/AI-Agent-GameApp";
        WebScrapingTool webScrapingTool = new WebScrapingTool();
        String res = webScrapingTool.scrapWebPage(url);
        Assertions.assertNotNull(res);
    }
}