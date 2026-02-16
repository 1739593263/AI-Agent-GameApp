package com.springai.polymodalaiagent.app;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GameAppTest {
    @Resource
    public GameApp gameApp;

    @Test
    void testChat() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是玩家VA";
        String answer = gameApp.doChat(message, chatId);
        // 第二轮
        message = "我想找一款休闲的类似RIMWORLD的游戏";
        answer = gameApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // 第三轮
        message = "我的想要找的游戏是什么来着？刚跟你说过，帮我回忆一下";
        answer = gameApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void testChatWithReport() {
        String chatId = UUID.randomUUID().toString();

        String message = "你好，我是玩家VA，我想找些休闲的类似RIMWORLD的游戏。请给我一个游戏介绍列表";
        GameApp.GameReport gameReport = gameApp.doChatWithReport(message, chatId);
        Assertions.assertNotNull(gameReport);
    }

    @Test
    void testChatWithRag() {
        String chatId = UUID.randomUUID().toString();
        long beforeTimeMillis = System.currentTimeMillis();
        String message = "你好，我是玩家VA，我想找一些PC端上的能招募伙伴的战旗类游戏";
        String answer = gameApp.doChatWithRag(message, chatId);
        long afterTimeMillis = System.currentTimeMillis();
        long gap = afterTimeMillis - beforeTimeMillis;
        System.out.println("time spending: "+gap);
        Assertions.assertNotNull(answer);
    }

    @Test
    void testChatWithTools() {
        String message1 = "查找并下载一张战略类游戏的图片";
        String chatId = UUID.randomUUID().toString();
        String gameRes1 = gameApp.doChatWithTools(message1, chatId);
        System.out.println("output: "+gameRes1);
        Assertions.assertNotNull(gameRes1);


        String message2 = "请搜索该游戏的相关介绍和攻略，并打印出pdf文件";
        String gameRes2 = gameApp.doChatWithTools(message2, chatId);
        System.out.println("output: "+gameRes2);
        Assertions.assertNotNull(gameRes2);
    }

    @Test
    void doChatWithMCPTools() {
//        String message = "searching the best rpg games for PC in 2025 based on GameBrain API";
        String message = "Please download some pictures of hot video games in this year";
        String chatId = UUID.randomUUID().toString();
        String gameRes1 = gameApp.doChatWithMCPTools(message, chatId);
        Assertions.assertNotNull(gameRes1);
    }
}