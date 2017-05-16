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
echo $version > $targetDir/version.txt
echo {\"Version\": \"$version\"} > $webClientTargetDir/version.json
