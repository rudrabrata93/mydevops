FROM alpine:latest
RUN sudo apk update -y && apk add bind-tools -y
CMD ["dig","google.com"]
