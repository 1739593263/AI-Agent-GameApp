package com.springai.polymodalaiagent.controller;

import com.springai.polymodalaiagent.agent.WManus;
import com.springai.polymodalaiagent.app.GameApp;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;

@RestController
@RequestMapping("/ai")
public class AiController {
    @Resource
    private GameApp gameApp;

    @Resource
    private ToolCallback[] toolCallbacks;

    @Resource
    private ChatModel dashscopeModel;

    @GetMapping("/game_app/chat/sync")
    public String doChatWithAppSync(String query, String chatId) {
        String res = gameApp.doChat(query, chatId);
        return res;
    }

    @GetMapping(value = "/game_app/chat/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> doChatWithAppByStream(String query, String chatId) {
        return gameApp.doChatWithStream(query, chatId);
    }

    @GetMapping(value = "/game_app/chat/serversent")
    public Flux<ServerSentEvent<String>> doChatWithAppByServerSentEvent(String query, String chatId){
        return gameApp.doChatWithStream(query, chatId)
                .map(chunk -> ServerSentEvent.<String>builder()
                        .data(chunk)
                        .build());
    }


    @GetMapping(value = "/game_app/chat/sseemitter")
    public SseEmitter doChatWithAppBySSEEmitter(String query, String chatId) {
        SseEmitter sseEmitter = new SseEmitter(180000L); // 三分钟超时时间
        gameApp.doChatWithStream(query, chatId)
                .subscribe(chunk -> {
                    try {
                        sseEmitter.send(chunk);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                },
                        sseEmitter::completeWithError, // 输出错误消息
                        sseEmitter::complete // 发送完成调用完成方法
                );
        return sseEmitter;
    }

    @GetMapping(value = "/game_app/wmanus/sseemitter")
    public SseEmitter doChatWithWManusStream(String query) {
        WManus manus = new WManus(toolCallbacks, dashscopeModel);
        return manus.runWithStream(query);
    }
}
