package com.springai.polymodalaiagent.agent;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.springai.polymodalaiagent.advisor.MyLoggerAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Component;

/**
 * WManus智能体
 */
@Component
public class WManus extends ToolCallAgent{
    public WManus(ToolCallback[] allTools, ChatModel dashScopeChatModel) {
        super(allTools);
        this.setName("WManus");
        String SYSTEM_PROMPT = """
                You are WManus, an all-capable AI assistant, aimed at solving any task presented by the user.
                You have various tools at your disposal that you can call upon to efficiently complete complex requests.
                """;
        this.setSystemPrompt(SYSTEM_PROMPT);
        String NEXT_STEP_PROMPT = """
                Based on user needs, proactively select the most appropriate tool or combination of tools.
                For complex tasks, you can break down the problem and use different tools step by step to solve it.
                After using each tool, clearly explain the execution results and suggest the next steps.
                If you want to stop the interaction at any point, use the `terminate` tool/function call.
                """;
        this.setNextStepPrompt(NEXT_STEP_PROMPT);
        this.setMaxSteps(3); // 最多调用n次工具来完成回答
        // 注意此处不要直接注入Tools，因为ToolCallAgent用toolCallingManager手动检索并执行了Tool工具，如果此处注入，chatClient会自己自动执行一般工具调用
        ChatClient chatClient = ChatClient.builder(dashScopeChatModel)
                .defaultAdvisors(new MyLoggerAdvisor())
                .build();
        this.setChatClient(chatClient);
    }
}
