#!/usr/bin/env bash
set -e

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

cat > src/main/scala/upstream/Placeholder.scala << 'EOF'
package upstream
EOF
git add .
git commit -m "initial commit"

# Create feature branch with Greeter class
git checkout -b feature

cat > src/main/scala/upstream/Greeter.scala << 'EOF'
package upstream

object Greeter {
  def greet: String = "hello-from-feature"
}
EOF
git add .
git commit -m "add Greeter on feature branch"
