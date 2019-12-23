FROM centos:centos7

MAINTAINER Mathias Schilling <m@matchilling.com>

ENV JAVA_VERSION="1.8.0-openjdk"

ENV IMAGEMAGICK_VERSION="7.0.9-10"
ENV IMAGEMAGICK_LIB_RPM_URL="https://imagemagick.org/download/linux/CentOS/x86_64/ImageMagick-libs-${IMAGEMAGICK_VERSION}.x86_64.rpm"
ENV IMAGEMAGICK_RPM_URL="https://imagemagick.org/download/linux/CentOS/x86_64/ImageMagick-${IMAGEMAGICK_VERSION}.x86_64.rpm"

RUN export HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" $IMAGEMAGICK_RPM_URL)
RUN if [ $HTTP_CODE != "200" ] ; then echo "$IMAGEMAGICK_RPM_URL does not exist" && exit 1 ; fi

RUN curl -s -S "https://download-ib01.fedoraproject.org/pub/epel/7/x86_64/Packages/l/libraqm-0.7.0-1.el7.x86_64.rpm" -o libraqm.rpm && \
    curl -s -S $IMAGEMAGICK_LIB_RPM_URL -o imagemagick-lib.rpm && \
    curl -s -S $IMAGEMAGICK_RPM_URL -o imagemagick.rpm

RUN yum -y update && \
    yum -y install "java-${JAVA_VERSION}" && \
    yum localinstall -y libraqm.rpm imagemagick*.rpm && \
    rm -f imagemagick*.rpm && \
    yum clean all

ARG JAR_FILE
COPY ${JAR_FILE} app.jar

# This is a bit of a hack but it ensures that the MemeGenerator can pass file
# references to the ImageMagick tools without having to stream everything back
# and forth as we are running in a JAR.
RUN mkdir -p "src/main/resources/meme/font/"
COPY "meme/font/" "src/main/resources/meme/font/"

RUN mkdir -p "src/main/resources/meme/image/"
COPY "meme/image/" "src/main/resources/meme/image/"

CMD ["sh","-c","java -Dserver.port=$PORT -Xmx300m -Xss512k -XX:CICompilerCount=2 -Dfile.encoding=UTF-8 -XX:+UseContainerSupport -Djava.security.egd=file:/dev/./urandom -jar /app.jar"]