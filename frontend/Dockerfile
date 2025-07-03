FROM node:18

WORKDIR /app

COPY package*.json .

RUN npm install

COPY . .

RUN npm install -g serve

ARG VITE_API_URL
ENV VITE_API_URL=$VITE_API_URL

RUN npm run build

CMD ["serve", "-s", "dist", "-l", "3000"]