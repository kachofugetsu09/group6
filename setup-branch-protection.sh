#!/bin/bash
# 设置GitHub分支保护规则的脚本

# 您需要先安装GitHub CLI: https://cli.github.com/
# 然后运行: gh auth login

echo "🔐 正在设置分支保护规则..."

# 检查是否已登录GitHub CLI
if ! gh auth status > /dev/null 2>&1; then
    echo "❌ 请先登录GitHub CLI:"
    echo "   1. 安装GitHub CLI: https://cli.github.com/"
    echo "   2. 运行: gh auth login"
    exit 1
fi

# 获取仓库信息
REPO=$(gh repo view --json nameWithOwner -q .nameWithOwner)
echo "📦 设置仓库: $REPO"

# 设置main分支保护规则
echo "🛡️  设置main分支保护规则..."

gh api \
  --method PUT \
  -H "Accept: application/vnd.github+json" \
  -H "X-GitHub-Api-Version: 2022-11-28" \
  "/repos/$REPO/branches/main/protection" \
  -f required_status_checks='{"strict":true,"contexts":["build-and-test"]}' \
  -f enforce_admins=true \
  -f required_pull_request_reviews='{"required_approving_review_count":1,"dismiss_stale_reviews":true,"require_code_owner_reviews":false}' \
  -f restrictions=null \
  -f allow_force_pushes=false \
  -f allow_deletions=false

if [ $? -eq 0 ]; then
    echo "✅ 分支保护规则设置成功！"
    echo ""
    echo "📋 现在的保护规则："
    echo "   • 必须通过CI检查 (build-and-test)"
    echo "   • 需要至少1个审查批准"
    echo "   • 禁止强制推送"
    echo "   • 禁止删除分支"
    echo ""
    echo "🚫 现在无法直接推送到main分支，必须通过Pull Request！"
else
    echo "❌ 设置失败，请检查权限"
fi
