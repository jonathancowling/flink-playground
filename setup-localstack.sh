#!/usr/bin/env bash

aws --endpoint http://localhost:4566 kinesis create-stream --stream-name input --shard-count 1
aws --endpoint http://localhost:4566 s3 mb s3://output   
for i in 1 2 3 4 5 6 7 8 9 0; do aws --endpoint http://localhost:4566 kinesis put-record --stream-name input --data "$( base64 <<< "This is test $i" )" --partition-key test-partition; done