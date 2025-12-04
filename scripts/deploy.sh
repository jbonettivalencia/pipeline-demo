#!/usr/bin/env bash
set -e

ENV="$1"

if [ -z "$ENV" ]; then
  echo "Usage: scripts/deploy.sh [staging|prod]"
  exit 1
fi

if [ "$ENV" = "staging" ]; then
  PORT=8081
elif [ "$ENV" = "prod" ]; then
  PORT=8082
else
  echo "Unknown environment: $ENV"
  exit 1
fi

CLASSES_DIR="target/classes"
PID_FILE="app-${ENV}.pid"
LOG_FILE="app-${ENV}.log"

# Stop old instance if running
if [ -f "$PID_FILE" ]; then
  OLD_PID=$(cat "$PID_FILE")
  if ps -p "$OLD_PID" > /dev/null 2>&1; then
    echo "Stopping existing ${ENV} instance (PID ${OLD_PID})"
    kill "$OLD_PID" || true
    sleep 1
  fi
fi

echo "Starting ${ENV} instance on port ${PORT} ..."
export PORT=$PORT
nohup java -cp "$CLASSES_DIR" com.example.App > "$LOG_FILE" 2>&1 &

NEW_PID=$!
echo "$NEW_PID" > "$PID_FILE"

echo "${ENV} started with PID ${NEW_PID}. Logs: ${LOG_FILE}"
