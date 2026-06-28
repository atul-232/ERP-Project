# Use Java 21 JDK base image (contains compiler javac and packaging tool jar)
FROM eclipse-temurin:21-jdk-jammy

# Install virtual frame buffer (Xvfb), lightweight window manager (fluxbox), VNC server (x11vnc), websockify, and noVNC
RUN apt-get update && apt-get install -y \
    xvfb \
    x11vnc \
    fluxbox \
    novnc \
    websockify \
    && rm -rf /var/lib/apt/lists/*

# Set up noVNC to load vnc.html automatically as index.html
RUN cp /usr/share/novnc/vnc.html /usr/share/novnc/index.html

# Set working directory inside container
WORKDIR /app

# Copy all repository source files and folders (edu, lib, resources, scripts)
COPY . /app

# Compile the Java source code and bundle it into ERP-System.jar inside the container
RUN chmod +x /app/build.sh && /app/build.sh

# Expose Render default port
EXPOSE 10000

# Run entrypoint script
CMD ["/app/entrypoint.sh"]
