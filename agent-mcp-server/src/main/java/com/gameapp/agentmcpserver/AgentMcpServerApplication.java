package com.gameapp.agentmcpserver;

import com.gameapp.agentmcpserver.tools.ImageSearchTool;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AgentMcpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgentMcpServerApplication.class, args);
    }

    @Bean
    public ToolCallbackProvider imgSearchTool(ImageSearchTool imageSearchTool) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(imageSearchTool)
                .build();
    }
}
