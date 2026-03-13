#!/usr/bin/env bash
set -e

cd upstream-repo
git config commit.gpgsign false

cat > src/main/scala/upstream/Helper.scala << 'EOF'
package upstream

object Helper {
  def help: String = "helped"
}
EOF

git add .
git commit -m "add Helper class"
