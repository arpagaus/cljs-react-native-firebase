NAME = expo.io-cli
VERSION = 44.0.0

.PHONY: all build run

all: build

build:
	 docker build -t $(NAME):$(VERSION) --pull .

run:
	docker run -p 19000:19000 -p 19001:19001 -v $(PWD)/..:/code --rm $(NAME):$(VERSION) 'exp' 'start' '--lan'

