
docker-compose exec kafka kafka-topics.sh --create --topic baeldung_linux `
  --partitions 1 --replication-factor 1 --bootstrap-server kafka:9092