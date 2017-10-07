FROM node:6
RUN mkdir -p /code
WORKDIR /code

RUN npm set progress=false && \
    npm install -g exp
RUN exp login --username 'arpagaus.remo@gmail.com' --password maria56

EXPOSE 19000
EXPOSE 19001

