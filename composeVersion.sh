baseVersion=`cat baseVersion.txt`
commitCount=$(git rev-list --count HEAD)
commit=$(git rev-parse --short HEAD)

version="${baseVersion}-${commitCount}-${commit}"

echo Version: $version

rootDir=`dirname $0`
targetDir=$rootDir/generated
webClientTargetDir=$rootDir/WebClient/generated
rm -rf $targetDir
rm -rf $webClientTargetDir
mkdir $targetDir
mkdir $webClientTargetDir
echo Version=$version > $targetDir/version.properties
echo {\"Version\": \"$version\"} > $webClientTargetDir/version.json