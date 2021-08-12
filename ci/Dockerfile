FROM openjdk:11
RUN useradd -ms /bin/bash sonar
RUN mkdir /work && chown sonar:sonar /work
USER sonar
WORKDIR /work
COPY get_sonar.sh configure_sonar.sh e2e_sonar.sh /work/
ENTRYPOINT ["./e2e_sonar.sh"]