package com.springai.polymodalaiagent.agent.model;

/**
 * Agent 执行的状态枚举
 */
public enum AgentState {
    /**
     * 空闲状态
     */
    IDLE,

    /**
     * 运行中状态
     */
    RUNNING,

    /**
     * 已完成状态
     */
    FINISHED,

    /**
     * 错误状态
     */
    ERROR
}
