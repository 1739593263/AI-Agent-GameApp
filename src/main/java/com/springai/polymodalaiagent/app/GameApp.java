package com.springai.polymodalaiagent.app;

import cn.hutool.core.lang.tree.TreeBuilder;
import com.springai.polymodalaiagent.advisor.MyLoggerAdvisor;
import com.springai.polymodalaiagent.chatmemory.FileBasedChatMemory;
import com.springai.polymodalaiagent.rag.AppRagCustomAdvisorFactory;
import com.springai.polymodalaiagent.rag.QueryRewriter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.AdvisorParams;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class GameApp {
    private final ChatClient chatClient;

    private static final String SYSTEM_PROMPT = "扮演玩过各种游戏的全能玩家。开场向用户表明身份，告知用户可查找游戏攻略和咨询相关游戏。\n"  +
            "根据对话识别玩家类型：\n" +
            "硬核型：追求成就、机制、效率 → 直接上深度攻略、数值、逃课邪道\n" +
            "休闲型：怕难、怕复杂、为放松 → 给轮椅流派、逃课方案、降低门槛\n" +
            "迷茫型：不知道玩什么、求推荐 → 追问口味偏好、设备条件、精准安利\n" +
            "社交型：想找人玩、问联机组队 → 给联机攻略、社交破冰、推荐联机游戏\n" +
            "识别后切入对应提问，引导用户详述需求、设备、过往偏好、卡点，收齐信息后给出具体可执行的专属建议。口语化，带梗，不堆砌术语。";

    @Resource
    private VectorStore GameAppVectorStore;

//    @Resource
//    private VectorStore PgVectorStore;

    @Resource
    private Advisor GameAppRagCloudAdvisor;

    @Resource
    private QueryRewriter queryRewriter;

    @Resource
    private ToolCallback[] allTools;

    @Resource
    private ToolCallbackProvider toolCallbackProvider;

    public GameApp(ChatModel dashscopeChatModel) {
        // 初始化基于文件对话记忆
        String fileDir = System.getProperty("user.dir")+"/tmp/chat-memory";
//        ChatMemory chatMemory = new FileBasedChatMemory(fileDir);

        // 初始化基于内存的对话记忆
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .maxMessages(10)
                .build();
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

    public Flux<String> doChatWithStream(String message, String chatId) {
        Flux<String> flux = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                .stream()
                .content();

//        log.info("content: {}", content);
        return flux;
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
        String transformedMessage = queryRewriter.rewrite(message);

        ChatResponse chatRagResponse = chatClient
                .prompt()
                .user(transformedMessage)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
//                .advisors(QuestionAnswerAdvisor.builder(GameAppVectorStore)
//                        .searchRequest(SearchRequest.builder().similarityThreshold(0.8).topK(5).build())
//                        .build()) // rag基于本地知识库 耗时 23343ms
                .advisors(GameAppRagCloudAdvisor) // rag 基于 Cloud知识库 耗时 39353ms
//                .advisors(QuestionAnswerAdvisor.builder(PgVectorStore)
//                        .searchRequest(SearchRequest.builder().similarityThreshold(0.8).topK(5).build())
//                        .build()) // rag基于PgVector数据库 耗时 10630ms
//                .advisors(AppRagCustomAdvisorFactory.createAppCustomAdvisor(
//                        GameAppVectorStore, "休闲"
//                )) // rag 基于 自定义RetrievalArgumentAdvisor（查询增强服务）
                .call()
                .chatResponse();

        log.info("content: {}", chatRagResponse);
        return chatRagResponse.getResult().getOutput().getText();
    }

    public String doChatWithTools(String message, String chatId) {
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                .toolCallbacks(allTools)
                .call()
                .chatResponse();

//        log.info("content: {}", chatReportResponse);
        return chatResponse.getResult().getOutput().getText();
    }


    public String doChatWithMCPTools(String message, String chatId) {
        ToolCallback[] callbacks = toolCallbackProvider.getToolCallbacks();
        System.out.println(callbacks.length);
        log.info("Available MCP tools: {}", Arrays.stream(callbacks).toString());
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                .toolCallbacks(toolCallbackProvider)
//                .toolCallbacks(allTools) // 配合Tools使用
                .call()
                .chatResponse();

//        log.info("content: {}", chatReportResponse);
        return chatResponse.getResult().getOutput().getText();
    }
}
