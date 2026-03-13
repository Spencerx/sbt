#!/usr/bin/env bash
set -e

cd upstream-repo
git config commit.gpgsign false

cat > src/main/scala/upstream/Utils.scala << 'EOF'
package upstream

object Utils {
  def util: String = "utility"
}
EOF

git add .
git commit -m "add Utils class"
