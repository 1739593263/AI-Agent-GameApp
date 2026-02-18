<template>
  <div class="chat-page">
    <header class="chat-header">
      <router-link to="/" class="back-btn">← 返回</router-link>
      <h1>AI 超级智能体</h1>
      <span class="chat-id">会话: {{ chatId }}</span>
    </header>
    <ChatRoom
      :messages="messages"
      :loading="loading"
      @send="handleSend"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import ChatRoom from '../components/ChatRoom.vue'
import { generateChatId } from '../utils/chatId.js'
import { chatWithWManusStream } from '../api/chat.js'

const chatId = ref('')
const messages = ref([])
const loading = ref(false)

onMounted(() => {
  chatId.value = generateChatId()
})

async function handleSend(text) {
  messages.value.push({ role: 'user', content: text })
  messages.value.push({ role: 'assistant', content: '' })
  loading.value = true
  const aiIndex = messages.value.length - 1

  await chatWithWManusStream(text, {
    onChunk: (chunk) => {
      messages.value[aiIndex].content += chunk
    },
    onDone: () => {
      loading.value = false
    },
    onError: (err) => {
      messages.value[aiIndex].content += `\n\n[错误: ${err.message}]`
      loading.value = false
    }
  })
}
</script>

<style scoped>
.chat-page {
  height: 100%;
  display: flex;
  flex-direction: column;
  background: var(--bg-primary);
}

.chat-header {
  display: flex;
  align-items: center;
  gap: 1.25rem;
  padding: 1rem 2rem;
  background: var(--bg-secondary);
  border-bottom: 1px solid var(--border-subtle);
}

.back-btn {
  color: var(--text-secondary);
  text-decoration: none;
  font-size: 0.9rem;
  letter-spacing: 0.03em;
  transition: color 0.2s;
}

.back-btn:hover {
  color: var(--accent);
}

.chat-header h1 {
  flex: 1;
  font-size: 1.1rem;
  font-weight: 500;
  letter-spacing: 0.05em;
  color: var(--text-primary);
}

.chat-id {
  font-size: 0.75rem;
  font-weight: 400;
  letter-spacing: 0.05em;
  color: var(--text-muted);
}
</style>
