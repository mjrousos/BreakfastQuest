FROM mhart/alpine-node

# Install NPM packages for server use
COPY package.json /tmp/package.json
RUN cd /tmp && npm install --production

# Copy ./Dist into the image
RUN mkdir -p /usr/src/app && mv /tmp/node_modules /usr/src
WORKDIR /usr/src/app
COPY ./dist /usr/src/app

#Expose the necessary port and launch the Node app
EXPOSE 5000
CMD node app.js
