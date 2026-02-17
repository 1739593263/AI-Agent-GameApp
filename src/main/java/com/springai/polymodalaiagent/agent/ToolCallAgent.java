package com.springai.polymodalaiagent.agent;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.springai.polymodalaiagent.agent.model.AgentState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.model.tool.ToolExecutionResult;
import org.springframework.ai.tool.ToolCallback;

import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class ToolCallAgent extends ReActAgent {

    private final ToolCallback[] toolCallbacks;

    private ChatResponse toolChatResponse;

    // 工具调用管理者 -- 用于分割工具调用和执行步骤。
    private final ToolCallingManager toolCallingManager;

    // 禁用 Spring AI 内置的工具调用机制，自己维护选项和消息上下文
    private final ChatOptions chatOptions;

    public ToolCallAgent(ToolCallback[] toolCallbacks) {
        super();
        this.toolCallbacks = toolCallbacks;
        this.toolCallingManager = ToolCallingManager.builder().build();
        this.chatOptions = DashScopeChatOptions.builder()
                .withInternalToolExecutionEnabled(false) // 重要！不然AI Model会走内部工具调用toolCallbacks
                .build();
    }

    @Override
    public boolean think() {
        // 1. 校验并拼接用户提示词，编辑chatMemory（getMessageList）
        if (StrUtil.isNotBlank(getNextStepPrompt())) {
            UserMessage userMessage = new UserMessage(getNextStepPrompt());
            getMessageList().add(userMessage);
        }

        // 2. 调用AI大模型，获取其结果
        List<Message> messageList = getMessageList();
        Prompt prompt = new Prompt(messageList, this.chatOptions);
        try {
            ChatResponse chatResponse = getChatClient().prompt(prompt)
                    .system(getSystemPrompt())
                    .toolCallbacks(toolCallbacks)
                    .call()
                    .chatResponse();
            this.toolChatResponse = chatResponse;
            // 3. 解析工具调用结果
            AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
            List<AssistantMessage.ToolCall> toolCalls = assistantMessage.getToolCalls();

            String result = assistantMessage.getText();
            log.info(getName() + "的思考：" + result);
            log.info(getName() + "选择了 " + toolCalls.size() + " 个工具来使用");
            String toolCallInfo = toolCalls.stream()
                    .map(toolCall -> String.format("工具名称：%s，参数：%s", toolCall.name(), toolCall.arguments()))
                    .collect(Collectors.joining("\n"));
            log.info(toolCallInfo);

            // 如果不需要调用工具，返回 false
            if (toolCalls.isEmpty()) {
                // 只有不调用工具时，才需要手动记录助手消息
                getMessageList().add(assistantMessage);
                return false;
            } else {
                // 需要调用工具时，无需记录助手消息，因为调用工具时会自动记录
                return true;
            }
        } catch (Exception e) {
            log.error(getName() + "的思考过程遇到了问题：" + e.getMessage());
            getMessageList().add(new AssistantMessage("处理时遇到了错误：" + e.getMessage()));
            return false;
        }
    }

    @Override
    public String act() {
        if (toolCallbacks.length == 0) {
            return "没有工具需要调用";
        }

        // 1. 调用工具
        Prompt prompt = new Prompt(getMessageList(), this.chatOptions);
        ToolExecutionResult toolExecutionResult = toolCallingManager.executeToolCalls(prompt, toolChatResponse);

        // 2. 记录消息上下文: conversationHistory(包含了prompt实例， 助手消息，以及工具返回消息)
        setMessageList(toolExecutionResult.conversationHistory());
        ToolResponseMessage toolResponseMessage = (ToolResponseMessage) CollUtil.getLast(toolExecutionResult.conversationHistory());
        // 判断是否调用了terminate工具
        boolean ifDoTerminate = toolResponseMessage.getResponses().stream()
                .anyMatch(response -> response.name().equals("doTerminate"));
        if (ifDoTerminate) {
            setState(AgentState.FINISHED);
        }

        String ToolStrResult = toolResponseMessage.getResponses().stream()
                .map(response -> "工具：" + response.name() + " 返回的结果：" + response.responseData())
                .collect(Collectors.joining("\n"));

        log.info(ToolStrResult);
        return ToolStrResult;
    }
}
