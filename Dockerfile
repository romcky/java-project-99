FROM gradle:8.5.0-jdk21

WORKDIR /.

COPY /. .

RUN gradle instllDist

CMP ./build/install/app/bin/app

