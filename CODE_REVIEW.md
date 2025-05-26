# 代码审查和质量保证

## 自动化检查

### GitHub Actions CI/CD
- **触发条件**: 推送到 main/master 分支或创建 Pull Request
- **检查内容**:
  - ✅ 代码编译通过
  - ✅ Main类存在
  - ✅ 无编译错误
  - ✅ 运行测试（如果存在）

### 本地Git Hooks
- **Pre-push Hook**: 推送前自动检查编译状态
- 如果编译失败，将阻止推送

## 使用方法

### 1. 确保代码质量
在推送代码前，请确保：
```bash
# 本地编译测试
mvn clean compile

# 运行Main类测试
mvn exec:java -Dexec.mainClass="com.group6.Main"
```

### 2. 推送代码
```bash
git add .
git commit -m "your commit message"
git push origin main
```

### 3. 检查CI状态
推送后检查GitHub Actions的运行状态，确保所有检查都通过。

## 分支保护规则（需要仓库管理员设置）

建议在GitHub上设置以下分支保护规则：
- 要求Pull Request审查
- 要求状态检查通过
- 要求分支是最新的

## 常见问题

### Q: 推送被阻止了怎么办？
A: 检查编译错误，修复后重新推送

### Q: CI检查失败怎么办？
A: 查看GitHub Actions的日志，根据错误信息修复代码

### Q: Main类找不到？
A: 确保Main.java在正确的包路径下：`src/main/java/com/group6/Main.java`
