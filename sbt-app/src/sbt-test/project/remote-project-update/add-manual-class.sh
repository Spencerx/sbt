#!/usr/bin/env bash
set -e

cd upstream-repo
git config commit.gpgsign false

cat > src/main/scala/upstream/ManualClass.scala << 'EOF'
package upstream

object ManualClass {
  def value: String = "manual"
}
EOF

git add .
git commit -m "add ManualClass"
