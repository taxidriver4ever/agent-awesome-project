# 3. 使用Python代码
import whisper

# 加载模型，默认是"small"，还有"tiny", "base", "medium", "large"可选，越大越准越慢
model = whisper.load_model("base")

# 转录音频文件
result = model.transcribe("D:/video/新录音 3(1).m4a", language="zh")

# 打印结果
print(result["text"])