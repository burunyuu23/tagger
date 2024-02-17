FROM gradle:7.6.4-jdk17-alpine AS jar-build
WORKDIR /app
COPY . .
RUN gradle bootJar

FROM eclipse-temurin:17 as app-build
ENV RELEASE=17

WORKDIR /opt/build
COPY --from=jar-build /app/build/libs/*.jar ./application.jar

RUN java -Djarmode=layertools -jar application.jar extract
RUN $JAVA_HOME/bin/jlink \
         --add-modules `jdeps --ignore-missing-deps -q -recursive --multi-release ${RELEASE} --print-module-deps -cp 'dependencies/BOOT-INF/lib/*':'snapshot-dependencies/BOOT-INF/lib/*' application.jar` \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=2 \
         --output jdk

FROM debian:buster-slim

ARG BUILD_PATH=/opt/build
ENV JAVA_HOME=/opt/jdk
ENV PATH "${JAVA_HOME}/bin:${PATH}"

RUN groupadd --gid 1000 spring-app \
  && useradd --uid 1000 --gid spring-app --shell /bin/bash --create-home spring-app \
    && apt-get update && apt-get install -y locales && rm -rf /var/lib/apt/lists/* \
           && echo "ru_RU.UTF-8 UTF-8" > /etc/locale.gen \
           && locale-gen

ENV LANG ru_RU.UTF-8
ENV LANGUAGE ru_RU:ru
ENV LC_ALL ru_RU.UTF-8

USER spring-app:spring-app
WORKDIR /opt/workspace

COPY --from=app-build $BUILD_PATH/jdk $JAVA_HOME
COPY --from=app-build $BUILD_PATH/spring-boot-loader/ ./
COPY --from=app-build $BUILD_PATH/dependencies/ ./
COPY --from=app-build $BUILD_PATH/snapshot-dependencies/ ./
COPY --from=app-build $BUILD_PATH/application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]