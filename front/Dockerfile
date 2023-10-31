FROM node:20 as npm_serve

WORKDIR /app

COPY package*.json ./

RUN npm install
COPY . .

FROM node:20

WORKDIR /app

COPY --from=npm_serve /app .

EXPOSE 3000
CMD ["npm", "run", "dev"]
