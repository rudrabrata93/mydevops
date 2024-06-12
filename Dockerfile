FROM alpine:latest
RUN apk update && apk add bind-tools
ENTRYPOINT ["dig"]
CMD ["google.com"]
