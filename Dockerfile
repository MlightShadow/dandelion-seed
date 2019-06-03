FROM java:8-jdk
WORKDIR /work_dir
ENTRYPOINT ["java" ,"-jar","/work_dir/seed_server.jar"]