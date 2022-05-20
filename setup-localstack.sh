#!/usr/bin/env bash

aws --endpoint http://localhost:4566 kinesis create-stream --stream-name input --shard-count 1
aws --endpoint http://localhost:4566 s3 mb s3://output   
i=0; while [ "$i" -lt 100 ]; do aws --endpoint http://localhost:4566 kinesis put-record --stream-name input --data "$( base64 <<< "This is test $i" )" --partition-key test-partition; (( i += 1 )); done 