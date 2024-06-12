FROM alpine:latest
RUN sudo yum update -y && yum install dig -yum
CMD ["dig","google.com"]
