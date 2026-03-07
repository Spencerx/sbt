#!/usr/bin/env bash
set -e

cd upstream-repo
git config commit.gpgsign false

cat > src/main/scala/upstream/BranchOnly.scala << 'EOF'
package upstream

object BranchOnly {
  def value: String = "branch-only"
}
EOF

git add .
git commit -m "add BranchOnly on feature branch"
