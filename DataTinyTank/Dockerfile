# --- build stage: full image has the toolchain to compile bcrypt's native addon ---
FROM node:22-bookworm AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci --omit=dev
COPY . .

# --- runtime stage: slim image, just the built app ---
FROM node:22-bookworm-slim
WORKDIR /app
ENV NODE_ENV=production
ENV PORT=1337
COPY --from=build /app .
EXPOSE 1337
CMD ["node", "server.js"]
