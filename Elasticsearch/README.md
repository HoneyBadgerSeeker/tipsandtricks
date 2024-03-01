# List all indexes

curl -XGET http://localhost:9200/_aliases?pretty=true

# Query on field

curl -XGET http://localhost:9200/index_name/_search?q=fieldName:fieldValue
