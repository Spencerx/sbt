#!/usr/bin/env bash
set -e

cd upstream-repo
git config commit.gpgsign false

cat > src/main/scala/upstream/EveryClass.scala << 'EOF'
package upstream

object EveryClass {
  def value: String = "every"
}
EOF

git add .
git commit -m "add EveryClass"
