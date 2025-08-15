FROM node:24-alpine AS builder
WORKDIR /app
COPY package.json ./
RUN --mount=type=cache,target=/root/.npm npm install
COPY . .
RUN npm run build

FROM nginx:1.29-alpine AS runner
COPY --chown=nginx:nginx --from=builder /app/dist/gomaterials /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf
EXPOSE 8080
ENTRYPOINT ["nginx", "-c", "/etc/nginx/nginx.conf"]
CMD ["-g", "daemon off;"]