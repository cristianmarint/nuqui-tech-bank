
# docker-compose exec kafka kafka-topics.sh --create --topic deposits.transactions--partitions 1 --replication-factor 1 --bootstrap-server kafka:9092

# docker-compose exec kafka kafka-topics.sh --create --topic deposits.transfer --partitions 1 --replication-factor 1 --bootstrap-server kafka:9092

# docker-compose exec kafka kafka-topics.sh --create --topic deposits.transfer.scheduled --partitions 1 --replication-factor 1 --bootstrap-server kafka:9092


# # consume
# docker-compose exec kafka kafka-console-producer.sh --topic deposits.search --broker-list kafka:9092