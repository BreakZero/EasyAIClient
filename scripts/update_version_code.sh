#!/bin/sh

DEFAULT_FILE="./configs/version_info.properties"

if [ "$#" -lt 2 ]; then
    echo "Usage: $0 [file] <key> <new_value>"
    exit 1
fi

if [ "$#" -eq 2 ]; then
    FILE=$DEFAULT_FILE
    KEY=$1
    NEW_VALUE=$2
else
    FILE=$1
    KEY=$2
    NEW_VALUE=$3
fi

if [ ! -f "$FILE" ]; then
    echo "File not found: $FILE"
    exit 1
fi

# 增加.bak 为了兼容 mac 系统本地测试
sed -i.bak "s/^$KEY=.*/$KEY=$NEW_VALUE/" "$FILE"

echo "Updated $KEY to $NEW_VALUE in $FILE"
