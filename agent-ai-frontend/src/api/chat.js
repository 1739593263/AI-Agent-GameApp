// 开发时用 /api 走 Vite 代理；生产环境配置完整的 VITE_API_BASE_URL
const BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api'

function parseSSEStream(reader, decoder, { onChunk }) {
  let buffer = ''
  return (async () => {
    while (true) {
      const { done, value } = await reader.read()
      if (done) return
      buffer += decoder.decode(value, { stream: true })
      const lines = buffer.split('\n')
      buffer = lines.pop() || ''
      for (const line of lines) {
        if (line.startsWith('data:')) {
          const data = line.slice(5).trim()
          if (data) onChunk?.(data)
        } else if (line.trim() && !line.startsWith(':')) {
          onChunk?.(line.trim())
        }
      }
    }
  })()
}

/**
 * AI 游戏大师 - SSE 流式对话
 * @param {string} query - 用户输入
 * @param {string} chatId - 聊天室 ID
 * @param {function} onChunk - 收到数据块时的回调
 * @param {function} onDone - 完成时的回调
 * @param {function} onError - 错误时的回调
 */
export async function chatWithGameAppStream(query, chatId, { onChunk, onDone, onError }) {
  const url = `${BASE_URL}/ai/game_app/chat/sse?query=${encodeURIComponent(query)}&chatId=${encodeURIComponent(chatId)}`
  try {
    const response = await fetch(url, {
      method: 'GET',
      headers: { Accept: 'text/event-stream' }
    })
    if (!response.ok) throw new Error(`HTTP ${response.status}`)
    const reader = response.body.getReader()
    const decoder = new TextDecoder()
    await parseSSEStream(reader, decoder, { onChunk })
    onDone?.()
  } catch (err) {
    onError?.(err)
  }
}

/**
 * AI 超级智能体 - SSE 流式对话
 * @param {string} query - 用户输入
 * @param {function} onChunk - 收到数据块时的回调
 * @param {function} onDone - 完成时的回调
 * @param {function} onError - 错误时的回调
 */
export async function chatWithWManusStream(query, { onChunk, onDone, onError }) {
  const url = `${BASE_URL}/ai/game_app/wmanus/sseemitter?query=${encodeURIComponent(query)}`
  try {
    const response = await fetch(url, {
      method: 'GET',
      headers: { Accept: 'text/event-stream' }
    })
    if (!response.ok) throw new Error(`HTTP ${response.status}`)
    const reader = response.body.getReader()
    const decoder = new TextDecoder()
    await parseSSEStream(reader, decoder, { onChunk })
    onDone?.()
  } catch (err) {
    onError?.(err)
  }
}
