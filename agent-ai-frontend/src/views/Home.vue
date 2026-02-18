<template>
  <div class="home">
    <div class="space-bg" aria-hidden="true">
      <div class="stars stars-far"></div>
      <div class="stars stars-mid"></div>
      <div class="stars stars-near"></div>
      <div class="fixed-stars">
        <span class="fixed-star" v-for="n in 12" :key="n" :style="{ left: fixedStarPos[n-1][0], top: fixedStarPos[n-1][1], animationDelay: fixedStarPos[n-1][2] }"></span>
      </div>
      <div class="nebula"></div>
      <div class="shooting-star shooting-star-left"></div>
      <div class="shooting-star shooting-star-right"></div>
    </div>
    <header class="header">
      <p class="eyebrow">第九艺术 · 智能进化</p>
      <h1>游戏大师AI问答平台</h1>
      <ul class="intro-list">
        <li>为迷茫玩家推介游戏</li>
        <li>为硬核玩家攻略护航</li>
        <li>为休闲玩家找到净土</li>
      </ul>
      <p class="subtitle">选择要使用的 AI 应用</p>
    </header>
    <main class="app-grid">
      <router-link to="/game-master" class="app-card">
        <span class="card-icon">◇</span>
        <h2>AI 游戏大师</h2>
        <p>游戏攻略、技巧与智能问答</p>
      </router-link>
      <router-link to="/super-agent" class="app-card">
        <span class="card-icon">◆</span>
        <h2>AI 超级智能体</h2>
        <p>多工具协作的智能助手</p>
      </router-link>
    </main>
  </div>
</template>

<script setup>
// 恒星位置和闪烁延迟 [left%, top%, animation-delay]
const fixedStarPos = [
  ['8%', '15%', '0s'],
  ['25%', '8%', '1.2s'],
  ['75%', '12%', '2.5s'],
  ['92%', '22%', '0.8s'],
  ['15%', '45%', '3s'],
  ['55%', '35%', '1.8s'],
  ['88%', '55%', '2.2s'],
  ['5%', '72%', '1.5s'],
  ['42%', '68%', '0.5s'],
  ['95%', '78%', '2.8s'],
  ['30%', '85%', '1.9s'],
  ['70%', '92%', '2.1s']
]
</script>

<style scoped>
.home {
  min-height: 100%;
  background: #050510;
  padding: 4rem 2rem;
  position: relative;
  overflow: hidden;
}

/* 宇宙星空背景 */
.space-bg {
  position: absolute;
  inset: 0;
  background: radial-gradient(ellipse 80% 50% at 50% 0%, #0f1420 0%, #050510 40%, #030308 100%);
  pointer-events: none;
}

/* 多层星星 */
.stars {
  position: absolute;
  inset: 0;
  background-repeat: repeat;
}

.stars-far {
  background-image: radial-gradient(1px 1px at 20px 30px, rgba(255,255,255,0.4), transparent),
    radial-gradient(1px 1px at 40px 70px, rgba(255,255,255,0.3), transparent),
    radial-gradient(1px 1px at 50px 160px, rgba(255,255,255,0.35), transparent),
    radial-gradient(1px 1px at 90px 40px, rgba(255,255,255,0.3), transparent),
    radial-gradient(1px 1px at 130px 80px, rgba(255,255,255,0.4), transparent),
    radial-gradient(1px 1px at 160px 120px, rgba(255,255,255,0.25), transparent);
  background-size: 200px 200px;
  animation: stars-drift 120s linear infinite;
}

.stars-mid {
  background-image: radial-gradient(1.5px 1.5px at 100px 50px, rgba(255,255,255,0.6), transparent),
    radial-gradient(1.5px 1.5px at 200px 150px, rgba(255,255,255,0.5), transparent),
    radial-gradient(1.5px 1.5px at 300px 250px, rgba(255,255,255,0.55), transparent),
    radial-gradient(1.5px 1.5px at 400px 100px, rgba(255,255,255,0.45), transparent),
    radial-gradient(1.5px 1.5px at 500px 300px, rgba(255,255,255,0.5), transparent),
    radial-gradient(1.5px 1.5px at 50px 400px, rgba(255,255,255,0.4), transparent);
  background-size: 550px 450px;
  animation: stars-drift 80s linear infinite reverse;
}

.stars-near {
  background-image: radial-gradient(2px 2px at 150px 200px, rgba(34,211,238,0.5), transparent),
    radial-gradient(2px 2px at 350px 100px, rgba(255,255,255,0.7), transparent),
    radial-gradient(2px 2px at 250px 350px, rgba(255,255,255,0.6), transparent),
    radial-gradient(2px 2px at 450px 250px, rgba(34,211,238,0.4), transparent);
  background-size: 500px 400px;
  animation: stars-drift 60s linear infinite;
  animation-delay: -20s;
}

@keyframes stars-drift {
  from { transform: translateY(0); }
  to { transform: translateY(-200px); }
}

/* 星云 */
.nebula {
  position: absolute;
  inset: 0;
  background: radial-gradient(ellipse 70% 40% at 50% 10%, rgba(34, 211, 238, 0.04) 0%, transparent 50%),
    radial-gradient(ellipse 50% 60% at 80% 60%, rgba(99, 102, 241, 0.03) 0%, transparent 40%);
  animation: nebula-pulse 15s ease-in-out infinite;
}

@keyframes nebula-pulse {
  0%, 100% { opacity: 0.7; }
  50% { opacity: 1; }
}

/* 若隐若现的恒星 */
.fixed-stars {
  position: absolute;
  inset: 0;
  pointer-events: none;
}

.fixed-star {
  position: absolute;
  width: 3px;
  height: 3px;
  background: rgba(255, 255, 255, 0.6);
  border-radius: 50%;
  box-shadow: 0 0 6px rgba(255, 255, 255, 0.4);
  animation: twinkle 4s ease-in-out infinite;
}

.fixed-star:nth-child(4n) {
  width: 2px;
  height: 2px;
}

.fixed-star:nth-child(3n) {
  box-shadow: 0 0 8px rgba(34, 211, 238, 0.3);
}

@keyframes twinkle {
  0%, 100% { opacity: 0.3; }
  50% { opacity: 1; }
}

/* 流星 - 从左上飞往右下，左一条右一条 */
.shooting-star {
  position: absolute;
  width: 120px;
  height: 2px;
  background: linear-gradient(45deg, transparent 0%, rgba(255,255,255,0.5) 30%, rgba(255,255,255,0.9) 70%, rgba(255,255,255,0.7) 100%);
  top: -30px;
  left: -30px;
  transform-origin: left center;
  pointer-events: none;
}

.shooting-star-left {
  animation: shooting-left 10s ease-in-out infinite;
  animation-delay: 0s;
  transform: rotate(32deg);
}

.shooting-star-right {
  animation: shooting-right 18s ease-in-out infinite;
  animation-delay: 4s;
  transform: rotate(32deg);
  top: -90px;
  left: 15%;
}

@keyframes shooting-left {
  0% { opacity: 0; transform: translate(0, 0) rotate(32deg); }
  4% { opacity: 1; }
  13% { opacity: 0.5; transform: translate(35vw, 48vh) rotate(32deg); }
  16% { opacity: 0; transform: translate(40vw, 53vh) rotate(32deg); }
  100% { opacity: 0; transform: translate(40vw, 53vh) rotate(32deg); }
}

@keyframes shooting-right {
  0% { opacity: 0; transform: translate(0, 0) rotate(32deg); }
  4% { opacity: 1; }
  13% { opacity: 0.5; transform: translate(60vw, 58vh) rotate(32deg); }
  16% { opacity: 0; transform: translate(65vw, 63vh) rotate(32deg); }
  100% { opacity: 0; transform: translate(65vw, 63vh) rotate(32deg); }
}

.header {
  text-align: center;
  margin-bottom: 4rem;
  position: relative;
}

.eyebrow {
  font-size: 0.75rem;
  font-weight: 500;
  letter-spacing: 0.2em;
  color: var(--accent);
  text-transform: uppercase;
  margin-bottom: 1rem;
}

.header h1 {
  font-size: 2.5rem;
  font-weight: 300;
  letter-spacing: 0.06em;
  color: var(--text-primary);
}

.intro-list {
  list-style: none;
  margin-top: 1.5rem;
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 1.25rem 2.5rem;
}

.intro-list li {
  position: relative;
  padding-left: 1rem;
  font-size: 0.95rem;
  color: var(--text-secondary);
  letter-spacing: 0.03em;
}

.intro-list li::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 4px;
  background: var(--accent);
  border-radius: 50%;
}

.subtitle {
  color: var(--text-muted);
  margin-top: 2rem;
  font-size: 0.9rem;
  font-weight: 400;
}

.app-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 1.5rem;
  max-width: 720px;
  margin: 0 auto;
  position: relative;
}

.app-card {
  display: block;
  padding: 2.25rem 2rem;
  background: var(--bg-card);
  backdrop-filter: blur(12px);
  border: 1px solid var(--border-default);
  border-radius: var(--radius-lg);
  text-decoration: none;
  color: inherit;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.app-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-lg), 0 0 24px rgba(34, 211, 238, 0.08);
  border-color: var(--accent-dim);
  background: rgba(30, 30, 30, 0.95);
}

.card-icon {
  display: inline-block;
  font-size: 1.25rem;
  color: var(--text-muted);
  margin-bottom: 1.25rem;
  letter-spacing: 0.1em;
  transition: color 0.3s;
}

.app-card:hover .card-icon {
  color: var(--accent);
}

.app-card h2 {
  font-size: 1.25rem;
  font-weight: 500;
  letter-spacing: 0.04em;
  color: var(--text-primary);
  margin-bottom: 0.5rem;
}

.app-card p {
  color: var(--text-secondary);
  font-size: 0.9rem;
  line-height: 1.6;
}
</style>
