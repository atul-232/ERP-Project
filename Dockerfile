# Use Java 21 JRE base image (lightweight)
FROM eclipse-temurin:21-jre-jammy

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

# Copy the pre-built Fat JAR and resources folder
COPY ERP-System.jar /app/ERP-System.jar
COPY resources /app/resources

# Copy startup script
COPY entrypoint.sh /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh

# Expose Render default port
EXPOSE 10000

# Run entrypoint script
CMD ["/app/entrypoint.sh"]
