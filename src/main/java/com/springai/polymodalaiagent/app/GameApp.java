package com.springai.polymodalaiagent.app;

import com.springai.polymodalaiagent.advisor.MyLoggerAdvisor;
import com.springai.polymodalaiagent.chatmemory.FileBasedChatMemory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.AdvisorParams;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class GameApp {
    private final ChatClient chatClient;

    private static final String SYSTEM_PROMPT = "扮演玩过各种游戏的硬核玩家。开场向用户表明身份，告知用户可查找游戏攻略和咨询相关游戏。\n"  +
            "根据对话识别玩家类型：\n" +
            "硬核型：追求成就、机制、效率 → 直接上深度攻略、数值、逃课邪道\n" +
            "休闲型：怕难、怕复杂、为放松 → 给轮椅流派、逃课方案、降低门槛\n" +
            "迷茫型：不知道玩什么、求推荐 → 追问口味偏好、设备条件、精准安利\n" +
            "社交型：想找人玩、问联机组队 → 给联机攻略、社交破冰、推荐联机游戏\n" +
            "识别后切入对应提问，引导用户详述需求、设备、过往偏好、卡点，收齐信息后给出具体可执行的专属建议。口语化，带梗，不堆砌术语。";

    @Resource
    private VectorStore GameAppVectorStore;

    @Resource
    private Advisor GameAppRagCloudAdvisor;

    public GameApp(ChatModel dashscopeChatModel) {
        // 初始化基于文件对话记忆
        String fileDir = System.getProperty("user.dir")+"/tmp/chat-memory";
        ChatMemory chatMemory = new FileBasedChatMemory(fileDir);

        // 初始化基于内存的对话记忆
//        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder()
//                .chatMemoryRepository(new InMemoryChatMemoryRepository())
//                .maxMessages(10)
//                .build();
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build(),
                        // 自定义日志，输出info级别日志，并且更简便。
                        new MyLoggerAdvisor()
                )
                .build();
    }

    public String doChat(String message, String chatId) {
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
//        log.info("content: {}", content);
        return content;
    }

    // record快速生成类
    record GameReport(String title, List<String> output) {}

    public GameReport doChatWithReport(String message, String chatId) {
        GameReport chatReportResponse = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                .advisors(AdvisorParams.ENABLE_NATIVE_STRUCTURED_OUTPUT) // 原生结构输出，强制符合GameReport.class结构
                .call()
                .entity(GameReport.class);
//               .entity(new ParameterizedTypeReference<Map<String, Object>>() {}) 自定义Map输出格式
//        log.info("content: {}", chatReportResponse);
        return chatReportResponse;
    }

    public String doChatWithRag(String message, String chatId) {
        // 本地知识库
//        ChatResponse chatRagResponse = chatClient
//                .prompt()
//                .user(message)
//                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
//                .advisors(QuestionAnswerAdvisor.builder(GameAppVectorStore).build())
//                .call()
//                .chatResponse();

        // Cloud知识库
        ChatResponse chatRagResponse = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                .advisors(GameAppRagCloudAdvisor)
                .call()
                .chatResponse();
//        log.info("content: {}", chatRagResponse);
        return chatRagResponse.getResult().getOutput().getText();
    }
}
