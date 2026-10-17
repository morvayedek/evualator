#!/usr/bin/env sh
set -eu
cd "$(dirname "$0")"
mkdir -p build/classes build/test-classes
find src/main/java -name '*.java' > build/main-sources.txt
find src/test/java -name '*.java' > build/test-sources.txt
javac --release 17 -encoding UTF-8 -d build/classes @build/main-sources.txt
javac --release 17 -encoding UTF-8 -cp build/classes -d build/test-classes @build/test-sources.txt
for source in src/test/java/dev/santo/evaluator/*Test.java; do
    test_class=$(basename "$source" .java)
    java -cp build/classes:build/test-classes "dev.santo.evaluator.$test_class"
done
jar --create --file build/evaluator.jar --main-class dev.santo.evaluator.Main -C build/classes .
