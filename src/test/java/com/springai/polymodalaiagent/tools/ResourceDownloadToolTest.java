package com.springai.polymodalaiagent.tools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ResourceDownloadToolTest {

    @Test
    void downloadResource() {
        ResourceDownloadTool tool = new ResourceDownloadTool();
        String url = "https://i0.hdslb.com/bfs/archive/98f542c87d40f3d219abe1343e6fd3c2deda4f36.jpg";
        String fileName = "mount&blade.jpg";
        String result = tool.downloadResource(url, fileName);
        System.out.println(result);
        assertNotNull(result);
    }
}