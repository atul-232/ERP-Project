# public Web Deployment Guide (Docker + noVNC)

This guide explains how to deploy the Java Swing application to the web using **Docker** and **noVNC**. This configuration hosts the Java application on a virtual Linux display server in the cloud and streams the GUI directly to any web browser.

Recruiters and users will be able to visit a public URL (e.g. `https://erp-project.onrender.com`), click connect, and log in to the ERP desktop application directly in their browser without downloading Java or setting up MySQL.

---

## Deploying on Render (Free Tier)

**Render** allows you to deploy Docker containers completely for free. Follow these steps:

### Step 1: Push Configurations to GitHub
Make sure the latest files ([Dockerfile](file:///Users/atulpandey/.gemini/antigravity/scratch/ERP-Project/Dockerfile) and [entrypoint.sh](file:///Users/atulpandey/.gemini/antigravity/scratch/ERP-Project/entrypoint.sh)) are pushed to your GitHub repository (already done!).

### Step 2: Create a Render Web Service
1. Log in to [Render Dashboard](https://dashboard.render.com/).
2. Click **New +** and select **Web Service**.
3. Connect your GitHub account and select your **`ERP-Project`** repository.
4. Set the following service parameters:
   - **Name:** `university-erp` (or any custom name)
   - **Region:** Choose a region close to your database (e.g., Oregon or Frankfurt)
   - **Branch:** `main`
   - **Runtime:** **Docker** (Render will automatically detect your `Dockerfile`)
   - **Plan:** **Free**

### Step 3: Add Database Environment Variables
To keep your credentials secure, do NOT write them in `config.properties`. Instead, configure them as environment variables on Render.

1. Under the **Environment** tab on Render, add the following key-value pairs (using your Aiven MySQL database details):
   - `DB_HOST` = `your-aiven-db-host`
   - `DB_PORT` = `your-aiven-db-port`
   - `DB_USER` = `your-aiven-db-user` (e.g., `avnadmin`)
   - `DB_PASSWORD` = `your-aiven-db-password`
   - `DB_NAME` = `defaultdb` (enables single-database mode on the cloud)

2. Click **Save Changes**.

### Step 4: Deploy and Run!
1. Render will automatically start building the Docker image from your repository and deploy the service. This takes about 3–5 minutes on the free tier.
2. Once the service status changes to **Live**, click the public URL provided by Render (e.g., `https://university-erp.onrender.com`).
3. The browser will open the noVNC desktop stream. Click the **Connect** button, and the University ERP application startup screen will launch instantly!

---

## local Testing with Docker

If you want to test the Docker setup locally on your computer, make sure you have Docker Desktop installed, then run:

1. Build the Docker image:
   ```bash
   docker build -t erp-app .
   ```
2. Run the Docker container (passing your connection details):
   ```bash
   docker run -p 8080:10000 \
     -e DB_HOST=your-db-host \
     -e DB_PORT=3306 \
     -e DB_USER=root \
     -e DB_PASSWORD=password \
     -e DB_NAME=defaultdb \
     erp-app
   ```
3. Visit **`http://localhost:8080`** in your browser to interact with the containerized application.
