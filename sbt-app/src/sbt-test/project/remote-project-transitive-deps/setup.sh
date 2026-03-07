#!/usr/bin/env bash
set -e

rm -rf global/staging upstream-repo
mkdir -p upstream-repo/a/src/main/scala/upstream
mkdir -p upstream-repo/b/src/main/scala/upstream
cd upstream-repo

git init
git config user.email "test@test.com"
git config user.name "Test"
git config commit.gpgsign false

cat > build.sbt << 'EOF'
lazy val b = project.in(file("b"))
lazy val a = project.in(file("a")).dependsOn(b)
EOF

cat > b/src/main/scala/upstream/BService.scala << 'EOF'
package upstream

object BService {
  def provide: String = "from-B"
}
EOF

cat > a/src/main/scala/upstream/AService.scala << 'EOF'
package upstream

object AService {
  def provide: String = s"from-A-via-${BService.provide}"
}
EOF

git add .
git commit -m "initial commit"
