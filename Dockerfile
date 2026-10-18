<<<<<<< HEAD
FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

=======
FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

>>>>>>> e052f7a9b32ceba4ebd020358a624f0bd5006405
CMD ["sh", "-c", "java -jar target/*.jar"]