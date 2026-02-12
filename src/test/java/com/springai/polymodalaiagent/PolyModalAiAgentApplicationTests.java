package com.springai.polymodalaiagent;

import com.springai.polymodalaiagent.app.GameApp;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class PolyModalAiAgentApplicationTests {
    @Resource
    GameApp gameApp;

    @Test
    void testContext() {

    }

}
