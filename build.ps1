$ErrorActionPreference = 'Stop'
Push-Location $PSScriptRoot
try {
    New-Item -ItemType Directory -Force build/classes, build/test-classes | Out-Null
    $mainSources = @(Get-ChildItem src/main/java -Recurse -Filter '*.java' | ForEach-Object FullName)
    & javac --release 17 -encoding UTF-8 -d build/classes @mainSources
    if ($LASTEXITCODE -ne 0) { throw 'Main compilation failed' }
    $testSources = @(Get-ChildItem src/test/java -Recurse -Filter '*.java' | ForEach-Object FullName)
    & javac --release 17 -encoding UTF-8 -cp build/classes -d build/test-classes @testSources
    if ($LASTEXITCODE -ne 0) { throw 'Test compilation failed' }
    foreach ($testSource in $testSources) {
        $testClass = 'dev.santo.evaluator.' + [IO.Path]::GetFileNameWithoutExtension($testSource)
        & java -cp "build/classes$([IO.Path]::PathSeparator)build/test-classes" $testClass
        if ($LASTEXITCODE -ne 0) { throw "Tests failed: $testClass" }
    }
    & jar --create --file build/evaluator.jar --main-class dev.santo.evaluator.Main -C build/classes .
    if ($LASTEXITCODE -ne 0) { throw 'JAR packaging failed' }
} finally {
    Pop-Location
}
