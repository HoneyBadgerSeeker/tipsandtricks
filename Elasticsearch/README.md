# List all indexes

curl -XGET http://localhost:9200/_aliases?pretty=true

# Query on field

curl -XGET http://localhost:9200/index_name/_search?q=fieldName:fieldValue

# Add document to indice (with ID already defined)

curl -XPUT -H "Content-Type: application/json" -H "Cache-Control: no-cache" -d '{
                    "name": "value",
                    "name": "value"
}' "http://localhost:9200/index_name/contact/fieldId"

