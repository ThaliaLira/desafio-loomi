setup:
	docker compose build

up:
	docker compose up --build

up-detached:
	docker compose up -d --build

down:
	docker compose down

clean:
	docker compose down -v

build:
	./mvnw.cmd clean package -DskipTests

test:
	./mvnw.cmd test

logs:
	docker compose logs -f app

logs-all:
	docker compose logs -f

db:
	docker exec -it order-postgres psql -U postgres -d order_db

topics:
	docker exec -it order-redpanda rpk topic list

create-topic:
	docker exec -it order-redpanda rpk topic create order-events

consume-events:
	docker exec -it order-redpanda rpk topic consume order-events --offset end