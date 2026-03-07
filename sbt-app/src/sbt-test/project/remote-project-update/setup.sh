#!/usr/bin/env bash
set -e

rm -rf global/staging upstream-repo
mkdir -p upstream-repo/src/main/scala/upstream
cd upstream-repo
git init
git config user.email "test@test.com"
git config user.name "Test"
git config commit.gpgsign false

cat > build.sbt << 'EOF'
name := "upstream-dep"
organization := "upstream"
version := "0.1.0"
EOF

cat > src/main/scala/upstream/Greeter.scala << 'EOF'
package upstream

object Greeter {
  def greet: String = "hello"
}
EOF

git add .
git commit -m "initial commit"
