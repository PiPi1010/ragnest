#!/bin/zsh
# RagNest 本地 Ollama 一键启动脚本（原生 Metal GPU 加速）
# 用法：./start-ollama.sh

# 关键：清除 WorkBuddy 注入的沙箱代理（57455，会导致 x509 证书错误），
# 改用 ClashX 的真实代理（7890）来下载模型
unset HTTP_PROXY HTTPS_PROXY http_proxy https_proxy ALL_PROXY all_proxy

# 检测 ClashX 是否在运行，若在运行则走它的代理下载模型
if lsof -i :7890 -sTCP:LISTEN >/dev/null 2>&1; then
  export HTTPS_PROXY=http://127.0.0.1:7890
  export HTTP_PROXY=http://127.0.0.1:7890
  echo "✅ 检测到 ClashX 代理（7890），将用于下载模型"
else
  echo "⚠️ 未检测到 ClashX 代理，模型下载可能失败（需科学上网）"
fi

echo "=== 启动原生 Ollama（Metal GPU 加速）==="

# 如果已在运行，提示
if curl -s -m 2 http://127.0.0.1:11434/api/tags >/dev/null 2>&1; then
  echo "✅ Ollama 已在运行，无需重复启动"
else
  echo "启动 Ollama 服务..."
  OLLAMA_FLASH_ATTENTION=1 OLLAMA_KV_CACHE_TYPE=q8_0 \
    /opt/homebrew/opt/ollama/bin/ollama serve > /tmp/ollama.log 2>&1 &
  echo "Ollama 服务已启动（日志: /tmp/ollama.log）"
fi

# 等待服务就绪
echo "等待服务就绪..."
for i in {1..10}; do
  if curl -s -m 2 http://127.0.0.1:11434/api/tags >/dev/null 2>&1; then
    echo "✅ 服务已就绪"
    break
  fi
  sleep 1
done

# 检查模型
echo ""
echo "=== 检查模型 ==="
/opt/homebrew/opt/ollama/bin/ollama list 2>&1

# 清理可能残留的失败下载分片（避免损坏文件干扰）
echo ""
echo "=== 清理残留的失败下载分片 ==="
find ~/.ollama/models/blobs -name "*-partial*" -delete 2>/dev/null
echo "已清理"

# 如果没有模型，提示拉取
if ! /opt/homebrew/opt/ollama/bin/ollama list 2>/dev/null | grep -q "qwen2.5:7b"; then
  echo ""
  echo "⚠️ 未找到 qwen2.5:7b，开始拉取（首次约 4-5GB，请耐心等待）..."
  /opt/homebrew/opt/ollama/bin/ollama pull qwen2.5:7b
fi

if ! /opt/homebrew/opt/ollama/bin/ollama list 2>/dev/null | grep -q "bge-m3"; then
  echo ""
  echo "⚠️ 未找到 bge-m3，开始拉取（embedding 模型，约 1.2GB）..."
  /opt/homebrew/opt/ollama/bin/ollama pull bge-m3
fi

echo ""
echo "=== 完成 ==="
echo "Ollama 地址: http://localhost:11434"
echo "后端配置无需改动（已指向 localhost:11434）"
echo "现在可以启动 RagNest 后端了"
