#!/bin/sh

set -e

VERSION=$1
[ ! -n "$VERSION" ] && echo "Enter release version: " && read VERSION

# post-release
PR_VERSION=$2
[ ! -n "$PR_VERSION" ] && echo "Enter post-release version: " && read PR_VERSION

echo "Releasing $VERSION - are you sure? (y/n):" && read CONFIRM && [ "$CONFIRM" != "y" ] && exit 0

[ ! -n "$MVN" ] && MVN=mvn

$MVN versions:set -DnewVersion=$VERSION -DgenerateBackupPoms=false && \
git add -u . && git commit -m "$VERSION" && \
$MVN -Prelease deploy && git tag protostuff-$VERSION && \
$MVN versions:set -DnewVersion=$PR_VERSION -DgenerateBackupPoms=false && \
git add -u . && git commit -m "$PR_VERSION" && \
git push -u origin master && git push origin --tags
