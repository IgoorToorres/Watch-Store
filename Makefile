.PHONY: run dev stop logs build test clean

run:
	docker compose up --build

dev:
	docker compose up -d db
	SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/watch SPRING_DATASOURCE_USERNAME=watch SPRING_DATASOURCE_PASSWORD=watch ./mvnw spring-boot:run

stop:
	docker compose down

logs:
	docker compose logs -f

build:
	./mvnw -DskipTests package

test:
	./mvnw test

clean:
	./mvnw clean
