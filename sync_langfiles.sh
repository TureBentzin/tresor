#!/bin/bash

# Usage: ./sync_langfiles.sh rootfile.json [-v] [-t]
# -v for verbose output
# -t for test mode (does not modify files)

ROOT_FILE="$1"
VERBOSE=false
TEST_MODE=false

# Check if jq is installed
if ! command -v jq &> /dev/null; then
  echo "Error: jq is not installed. Please install jq to use this script."
  exit 1
fi

# Parse arguments
shift
while getopts "vt" opt; do
  case $opt in
    v) VERBOSE=true ;;
    t) TEST_MODE=true ;;
    *) echo "Usage: $0 rootfile.json [-v] [-t]"; exit 1 ;;
  esac
done

if [[ ! -f "$ROOT_FILE" ]]; then
  echo "Error: Root file '$ROOT_FILE' does not exist."
  exit 1
fi

# Extract directory and find matching files
ROOT_DIR=$(dirname "$ROOT_FILE")
LANG_FILES=$(find "$ROOT_DIR" -type f -name "langfile_*.json" ! -name "$(basename "$ROOT_FILE")")

if [[ -z "$LANG_FILES" ]]; then
  echo "No language files found matching 'langfile_*.json'."
  exit 0
fi

# Convert JSON to sorted keys (recursive)
extract_keys() {
  jq -c 'path(..) | map(tostring) | join(".")' "$1" | sort
}

ROOT_KEYS=$(extract_keys "$ROOT_FILE")

for FILE in $LANG_FILES; do
  echo "Processing: $FILE"

  SECONDARY_KEYS=$(extract_keys "$FILE")

  # Find missing keys
  MISSING_KEYS=$(comm -23 <(echo "$ROOT_KEYS") <(echo "$SECONDARY_KEYS"))

  # Find extra keys (to be deleted)
  EXTRA_KEYS=$(comm -13 <(echo "$ROOT_KEYS") <(echo "$SECONDARY_KEYS"))

  if [[ -n "$MISSING_KEYS" ]]; then
    echo "WARNING: Missing keys in $FILE"
    echo "$MISSING_KEYS"
  fi

  if [[ -n "$EXTRA_KEYS" ]]; then
    echo "Removing extra keys from $FILE"

    for KEY in $EXTRA_KEYS; do
      if [[ "$VERBOSE" == "true" ]]; then
        echo "Removing: $KEY"
      fi
      if [[ "$TEST_MODE" == "false" ]]; then
        jq "del(.$KEY)" "$FILE" > "$FILE.tmp" && mv "$FILE.tmp" "$FILE"
      fi
    done
  fi

  echo "Done processing $FILE"
done

echo "Sync complete."