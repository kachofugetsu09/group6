# 设置GitHub分支保护规则

为了确保有问题的代码不能直接推送到main分支，您需要在GitHub上设置分支保护规则：

## 步骤：

1. **进入GitHub仓库页面**
   - 打开您的GitHub仓库：https://github.com/YOUR_USERNAME/YOUR_REPO_NAME

2. **进入设置页面**
   - 点击仓库顶部的 `Settings` 标签
   - 在左侧菜单中点击 `Branches`

3. **添加分支保护规则**
   - 点击 `Add rule` 按钮
   - 在 `Branch name pattern` 中输入：`main`

4. **配置保护规则**
   勾选以下选项：
   
   ✅ **Require status checks to pass before merging**
   - 勾选 `Require branches to be up to date before merging`
   - 在搜索框中添加：`build-and-test`
   
   ✅ **Require pull request reviews before merging**
   - 设置 `Required number of reviews before merging`: 1
   
   ✅ **Dismiss stale pull request approvals when new commits are pushed**
   
   ✅ **Restrict pushes that create files**
   
   ✅ **Do not allow bypassing the above settings**

5. **保存规则**
   - 点击 `Create` 按钮

## 工作流程：

设置完成后，开发流程将变为：

1. **创建新分支进行开发**
   ```bash
   git checkout -b feature/your-feature-name
   ```

2. **在新分支上进行开发和提交**
   ```bash
   git add .
   git commit -m "你的提交信息"
   git push origin feature/your-feature-name
   ```

3. **创建Pull Request**
   - 在GitHub上创建PR
   - CI会自动运行检查编译和运行
   - 只有通过所有检查才能合并到main分支

4. **代码审查和合并**
   - 需要至少1个人审查代码
   - CI检查必须通过（编译成功、无红色错误）
   - 满足条件后才能合并到main分支

这样就能确保main分支始终保持可编译和可运行的状态！
