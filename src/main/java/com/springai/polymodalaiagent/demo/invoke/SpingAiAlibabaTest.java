package com.springai.polymodalaiagent.demo.invoke;


import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.springai.polymodalaiagent.demo.APIKeyDemo;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;

import java.util.List;

public class SpingAiAlibabaTest {

    public static void main(String[] args) {
        // 初始化 ChatModel
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(APIKeyDemo.API_KEY)
                .build();

        ChatModel chatModel = DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .build();
        SystemMessage systemMsg = new SystemMessage("你是一个有帮助的助手。");
        UserMessage userMsg = new UserMessage("你好，你好吗？");

        // 与聊天模型一起使用
        List<Message> messages = List.of(systemMsg, userMsg);
        Prompt prompt = new Prompt(messages);
        ChatResponse response = chatModel.call(prompt);
        System.out.println(response);
    }


}
