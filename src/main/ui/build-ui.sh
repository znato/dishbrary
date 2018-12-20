#!/bin/sh

echo "$PWD"

BUILD_DIR="../../../target/classes/static"

PROJECT_RELATIVE_PATH_TO_BASE="src/main/ui/"

if [ ! -d "$BUILD_DIR" ]; then
    mkdir -p $BUILD_DIR
fi

babel -o $BUILD_DIR/app_bundle.js app/

cp -r ./css/ ./images/ ./lib/ ./fonts/ ./index.html $BUILD_DIR