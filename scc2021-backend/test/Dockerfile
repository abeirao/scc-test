FROM node:12

WORKDIR /usr/src/app

# Bundle app source
COPY . .

EXPOSE 8080

ENV NODE_PATH /usr/local/lib/node_modules:$NODE_PATH

RUN set -ex && apk --no-cache add git

# ...
RUN npm install -g faker
RUN npm install -g node-fetch –save
RUN npm install -g https://github.com/preguica/artillery-plugin-metrics-by-endpoint.git

RUN npm install -g artillery --unsafe-perm=true --allow-root

RUN cd test
RUN cd unit
RUN artillery run utest-add_entity.yml
