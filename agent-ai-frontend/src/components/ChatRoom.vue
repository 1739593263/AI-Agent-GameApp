<template>
  <div class="chat-room">
    <div class="messages" ref="messagesRef">
      <div
        v-for="(msg, i) in messages"
        :key="i"
        :class="['message', msg.role]"
      >
        <div class="message-content">
          <span class="role-label">{{ msg.role === 'user' ? '我' : 'AI' }}</span>
          <div class="text">
            <span v-html="formatMessage(msg.content)"></span>
            <span v-if="loading && i === messages.length - 1 && msg.role === 'assistant'" class="cursor">▌</span>
          </div>
        </div>
      </div>
    </div>
    <div class="input-area">
      <textarea
        v-model="input"
        placeholder="输入消息..."
        rows="2"
        :disabled="loading"
        @keydown.enter.exact.prevent="send"
      />
      <button
        class="send-btn"
        :disabled="!input.trim() || loading"
        @click="send"
      >
        发送
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick, watch } from 'vue'

const props = defineProps({
  messages: { type: Array, default: () => [] },
  loading: { type: Boolean, default: false }
})

const emit = defineEmits(['send'])

const input = ref('')
const messagesRef = ref(null)

function formatMessage(text) {
  if (!text) return ''
  return text
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/\n/g, '<br>')
}

function send() {
  const text = input.value.trim()
  if (!text || props.loading) return
  input.value = ''
  emit('send', text)
}

watch(
  () => [...props.messages],
  () => {
    nextTick(() => {
      if (messagesRef.value) {
        messagesRef.value.scrollTop = messagesRef.value.scrollHeight
      }
    })
  },
  { deep: true }
)
</script>

<style scoped>
.chat-room {
  display: flex;
  flex-direction: column;
  height: 100%;
  background: var(--bg-primary);
}

.messages {
  flex: 1;
  overflow-y: auto;
  padding: 1.5rem 2rem;
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.message {
  display: flex;
  max-width: 85%;
}

.message.user {
  align-self: flex-end;
}

.message.ai,
.message.assistant {
  align-self: flex-start;
}

.message-content {
  padding: 1.125rem 1.5rem;
  border-radius: var(--radius-md);
  line-height: 1.7;
}

.message.user .message-content {
  background: #fff;
  color: #0a0a0a;
  border: 1px solid var(--border-default);
  box-shadow: var(--shadow-sm);
}

.message.ai .message-content,
.message.assistant .message-content {
  background: var(--bg-elevated);
  border: 1px solid var(--border-default);
  color: var(--text-primary);
}

.role-label {
  display: block;
  font-size: 0.7rem;
  font-weight: 500;
  letter-spacing: 0.08em;
  color: var(--text-muted);
  margin-bottom: 0.5rem;
}

.message.user .role-label {
  color: rgba(0, 0, 0, 0.5);
}

.message .text {
  white-space: pre-wrap;
  word-break: break-word;
  font-size: 0.95rem;
}

.message .text .cursor {
  display: inline;
  animation: blink 1s step-end infinite;
}

@keyframes blink {
  50% { opacity: 0; }
}

.input-area {
  display: flex;
  gap: 1rem;
  padding: 1.25rem 2rem;
  background: var(--bg-secondary);
  border-top: 1px solid var(--border-subtle);
}

.input-area textarea {
  flex: 1;
  padding: 1rem 1.25rem;
  border-radius: var(--radius-md);
  border: 1px solid var(--border-default);
  background: var(--bg-primary);
  color: var(--text-primary);
  font-size: 0.95rem;
  resize: none;
  font-family: inherit;
  transition: border-color 0.2s;
}

.input-area textarea::placeholder {
  color: var(--text-muted);
}

.input-area textarea:focus {
  outline: none;
  border-color: var(--accent-dim);
}

.send-btn {
  padding: 1rem 1.75rem;
  border-radius: var(--radius-md);
  border: 1px solid rgba(255, 255, 255, 0.25);
  background: #fff;
  color: #0a0a0a;
  font-weight: 500;
  font-size: 0.9rem;
  letter-spacing: 0.05em;
  cursor: pointer;
  align-self: flex-end;
  transition: all 0.2s;
}

.send-btn:hover:not(:disabled) {
  background: var(--accent);
  color: #0a0a0a;
  border-color: var(--accent);
}

.send-btn:disabled {
  opacity: 0.4;
  cursor: not-allowed;
}
</style>
