aws dynamodb create-table `
    --table-name users `
    --attribute-definitions `
        AttributeName=id,AttributeType=S `
        AttributeName=humanId,AttributeType=N `
        AttributeName=email,AttributeType=S `
        AttributeName=username,AttributeType=S `
    --key-schema AttributeName=id,KeyType=HASH AttributeName=humanId,KeyType=RANGE `
    --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 `
    --global-secondary-indexes `
        'IndexName=emailindex,KeySchema=[{AttributeName=email,KeyType=HASH}],Projection={ProjectionType=ALL},ProvisionedThroughput={ReadCapacityUnits=5,WriteCapacityUnits=5}' `
        'IndexName=usernameindex,KeySchema=[{AttributeName=username,KeyType=HASH}],Projection={ProjectionType=ALL},ProvisionedThroughput={ReadCapacityUnits=5,WriteCapacityUnits=5}' `
    --endpoint-url http://localhost:8000 `
    --profile default

aws dynamodb put-item --table-name users --item '{\"deletedAt\": {\"S\": \"\"},\"email\": {\"S\": \"celestine96@hotmail.com\"}, \"humanId\": {\"N\": \"7\"}, \"id\": {\"S\": \"12ebc8ee-ff66-4455-ad3b-6a7c7413ac97\"}, \"password\": {\"S\": \"password7\"}, \"recentActivity\": {\"S\": \"LOGIN SUCCESSFUL AT 2024-01-01T20:34:25.627823900Z\"}, \"status\": {\"S\": \"ACTIVE\"}, \"token\": {\"S\": \"eyJhbGciOiJub25lIn0.eyJzdWIiOiJ1c2VybmFtZSIsImlhdCI6MTcwNDE0MTI2NSwiZXhwIjoxNzA0MTQxMjk1fQ.\"}, \"username\": {\"S\": \"username\"}}' --endpoint-url http://localhost:8000 --profile default
aws dynamodb put-item --table-name users --item '{\"deletedAt\": {\"S\": \"\"},\"email\": {\"S\": \"carlos@hotmail.com\"}, \"humanId\": {\"N\": \"8\"}, \"id\": {\"S\": \"12ebc8ee-ff66-4455-ad3b-6a7c7413ac98\"}, \"password\": {\"S\": \"password8\"}, \"recentActivity\": {\"S\": \"LOGIN SUCCESSFUL AT 2024-01-01T20:34:25.627823900Z\"}, \"status\": {\"S\": \"ACTIVE\"}, \"token\": {\"S\": \"eyJhbGciOiJub25lIn0.eyJzdWIiOiJ1c2VybmFtZSIsImlhdCI6MTcwNDE0MTI2NSwiZXhwIjoxNzA0MTQxMjk1fQ.\"}, \"username\": {\"S\": \"carlos\"}}' --endpoint-url http://localhost:8000 --profile default
aws dynamodb put-item --table-name users --item '{\"deletedAt\": {\"S\": \"\"},\"email\": {\"S\": \"contact@nuqui.tech\"}, \"humanId\": {\"N\": \"69\"}, \"id\": {\"S\": \"12ebc8ee-ff66-4455-ad3b-6a7c7413ac69\"}, \"password\": {\"S\": \"password69\"}, \"recentActivity\": {\"S\": \"LOGIN SUCCESSFUL AT 2024-01-01T20:34:25.627823900Z\"}, \"status\": {\"S\": \"ACTIVE\"}, \"token\": {\"S\": \"eyJhbGciOiJub25lIn0.eyJzdWIiOiJ1c2VybmFtZSIsImlhdCI6MTcwNDE0MTI2NSwiZXhwIjoxNzA0MTQxMjk1fQ.\"}, \"username\": {\"S\": \"nuquitechfee\"}}' --endpoint-url http://localhost:8000 --profile default



