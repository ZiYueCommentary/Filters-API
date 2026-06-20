#!/bin/bash

set -e

R2_ENDPOINT="https://b7efa6738b0816b821bc0e7d8814b33b.r2.cloudflarestorage.com"
BUCKET="maven"

GROUP_ID=$(./gradlew -q printGroup)
ARTIFACT_ID=$(./gradlew -q printArtifact)
VERSION=$(./gradlew -q printVersion)

ENABLE_SHA256=true

REPO_TYPE="releases"   # releases or snapshots

if [ -z "$VERSION" ] || [ -z "$REPO_TYPE" ]; then
  echo "Usage: ./publish.sh <version> <releases|snapshots>"
  exit 1
fi

if [[ "$REPO_TYPE" != "releases" && "$REPO_TYPE" != "snapshots" ]]; then
  echo "REPO_TYPE must be 'releases' or 'snapshots'"
  exit 1
fi

GROUP_PATH=$(echo "$GROUP_ID" | tr '.' '/')
LOCAL_BASE="$HOME/.m2/repository/$GROUP_PATH/$ARTIFACT_ID"
LOCAL_VERSION_DIR="$LOCAL_BASE/$VERSION"

R2_BASE="s3://$BUCKET/$REPO_TYPE/$GROUP_PATH/$ARTIFACT_ID"

echo "🔨 Maven build + install"
./gradlew publishToMavenLocal

if [ ! -d "$LOCAL_VERSION_DIR" ]; then
  echo "❌ Version not found in local repository: $LOCAL_VERSION_DIR"
  exit 1
fi

if [ "$REPO_TYPE" = "releases" ]; then
  echo "🔍 Checking remote release existence..."

  if aws s3 ls "$R2_BASE/$VERSION/" \
    --endpoint-url=$R2_ENDPOINT >/dev/null 2>&1; then

    echo "❌ Release version already exists. Abort."
    exit 1
  fi
fi

echo "🔑 Generating checksums..."

generate_checksum() {
  for f in $(find "$1" -type f \( -name "*.jar" -o -name "*.pom" \)); do
    sha1sum "$f" | awk '{print $1}' > "$f.sha1"

    if [ "$ENABLE_SHA256" = true ]; then
      sha256sum "$f" | awk '{print $1}' > "$f.sha256"
    fi
  done
}

generate_checksum "$LOCAL_VERSION_DIR"

# artifact metadata
if [ -f "$LOCAL_BASE/maven-metadata.xml" ]; then
  sha1sum "$LOCAL_BASE/maven-metadata.xml" | awk '{print $1}' > "$LOCAL_BASE/maven-metadata.xml.sha1"

  if [ "$ENABLE_SHA256" = true ]; then
    sha256sum "$LOCAL_BASE/maven-metadata.xml" | awk '{print $1}' > "$LOCAL_BASE/maven-metadata.xml.sha256"
  fi
fi

echo "☁️ Uploading version files..."

aws s3 sync "$LOCAL_VERSION_DIR" \
  "$R2_BASE/$VERSION" \
  --endpoint-url=$R2_ENDPOINT

#echo "📦 Uploading metadata..."
#
#aws s3 cp "$LOCAL_BASE/maven-metadata.xml" \
#  "$R2_BASE/maven-metadata.xml" \
#  --endpoint-url=$R2_ENDPOINT
#
## metadata checksum
#if [ -f "$LOCAL_BASE/maven-metadata.xml.sha1" ]; then
#  aws s3 cp "$LOCAL_BASE/maven-metadata.xml.sha1" \
#    "$R2_BASE/maven-metadata.xml.sha1" \
#    --endpoint-url=$R2_ENDPOINT
#fi
#
#if [ "$ENABLE_SHA256" = true ] && [ -f "$LOCAL_BASE/maven-metadata.xml.sha256" ]; then
#  aws s3 cp "$LOCAL_BASE/maven-metadata.xml.sha256" \
#    "$R2_BASE/maven-metadata.xml.sha256" \
#    --endpoint-url=$R2_ENDPOINT
#fi

echo "✅ Publish completed!"
echo ""
echo "Repository:"
echo "  https://<your-domain>/$REPO_TYPE/$GROUP_PATH/$ARTIFACT_ID/$VERSION/"