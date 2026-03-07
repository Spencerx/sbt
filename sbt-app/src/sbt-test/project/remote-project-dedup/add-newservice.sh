#!/usr/bin/env bash
set -e

cd upstream-repo
git config commit.gpgsign false

cat > src/main/scala/upstream/NewService.scala << 'EOF'
package upstream

object NewService {
  def provide: String = "new-service"
}
EOF

git add .
git commit -m "add NewService"
