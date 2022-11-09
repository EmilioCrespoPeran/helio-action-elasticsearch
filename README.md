# Helio Action for Elasticsearch database

A component which executes [Elasticsearch](https://www.elastic.co/guide/index.html) operations.

The configuration must contain the following attributes:
* "operation" (String). Values admitted:
  * "query". Executes search request.
  * "create". Index new documents.
  * "update". Update a document.
  * "delete". Remove a document.
* "host" (String). Elasticsearch node location.
* "index" (String). Name of the index to be read or stored.

Data value is used with the previous configuration in order to execute querys.


# Register component

Use the following coordinates for importer the Elasticsearch action as an external component for the Helio ecosystem:

```json
{
    "source": "https://github.com/helio-ecosystem/helio-action-elasticsearch/releases/download/v0.1.1/helio-action-elasticsearch-0.1.1.jar",
    "clazz": "helio.actions.database.ElasticsearchAction",
    "type": "ACTION"
}
```
