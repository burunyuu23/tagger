FROM gradle:7.6.4-jdk17-alpine AS jar-build
ENV APP_HOME=/app
WORKDIR $APP_HOME
COPY build.gradle.kts settings.gradle.kts $APP_HOME

COPY gradle $APP_HOME/gradle
COPY --chown=gradle:gradle . /home/gradle/src
USER root
RUN chown -R gradle /home/gradle/src

RUN gradle build --stacktrace || return 0
COPY . .
RUN gradle clean bootJar

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
  && useradd --uid 1000 --gid spring-app --shell /bin/bash --create-home spring-app

USER spring-app:spring-app
WORKDIR /opt/workspace

COPY --from=app-build $BUILD_PATH/jdk $JAVA_HOME
COPY --from=app-build $BUILD_PATH/spring-boot-loader/ ./
COPY --from=app-build $BUILD_PATH/dependencies/ ./
COPY --from=app-build $BUILD_PATH/snapshot-dependencies/ ./
COPY --from=app-build $BUILD_PATH/application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]