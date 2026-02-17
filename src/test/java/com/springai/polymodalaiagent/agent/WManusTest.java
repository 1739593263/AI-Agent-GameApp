package com.springai.polymodalaiagent.agent;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WManusTest {

    @Resource
    WManus manus;

    @Test
    public void run() {
        String query = "请搜索游戏《空洞骑士》的相关介绍和攻略，并打印出pdf文件";
        String res = manus.run(query);
        Assertions.assertNotNull(res);
    }
}