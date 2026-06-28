#!/bin/bash
set -e

# Render assigns a port dynamically in the $PORT variable (defaults to 10000)
PORT_NUM=${PORT:-10000}

echo "=== Starting Virtual Display Server (Xvfb) ==="
Xvfb :99 -screen 0 1280x850x24 &
export DISPLAY=:99

sleep 2

echo "=== Starting Window Manager (fluxbox) ==="
fluxbox &

sleep 1

echo "=== Starting VNC Server (x11vnc) ==="
# Starts VNC server on display :99, listening on all interfaces, shared mode, no password
x11vnc -display :99 -forever -shared -nopw -xkb &

sleep 2

echo "=== Starting noVNC WebSocket Bridge on port $PORT_NUM ==="
# websockify bridges incoming WebSocket VNC traffic to TCP VNC port 5900
# Run with --verbose to log all connection handshakes and failures
websockify --verbose --web /usr/share/novnc $PORT_NUM 127.0.0.1:5900 &

sleep 1

echo "=== Launching University ERP Java Swing Application ==="
java -jar /app/ERP-System.jar
