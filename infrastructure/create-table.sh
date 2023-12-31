#!/bin/sh

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