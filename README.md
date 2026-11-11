# Products API

REST service for managing products, designed using the API First approach. The OpenAPI contract is available at [`src/main/resources/META-INF/openapi.yaml`](src/main/resources/META-INF/openapi.yaml).

## Requirements

- Java 21

## Run in development mode

On Windows:

```powershell
.\mvnw.cmd quarkus:dev
```

The API is available at `http://localhost:8080`.

## Endpoints

| Method | Path | Description | Responses |
|---|---|---|---|
| `GET` | `/products` | Lists all products. | `200` |
| `POST` | `/products` | Creates a product. | `201`, `400` |
| `GET` | `/products/{id}` | Retrieves a product by its UUID. | `200`, `404` |
| `DELETE` | `/products/{id}` | Deletes a product by its UUID. | `204`, `404` |

`404` error responses include an object with `code` and `message`, for example, `PRODUCT_NOT_FOUND`.

## Data model

To create a product, send a `NewProduct` object:

| Field | Type | Constraints |
|---|---|---|
| `name` | string | Required; between 2 and 100 characters. |
| `price` | number | Required; minimum `0.01`. |
| `stock` | integer | Optional; minimum `0`; defaults to `0`. |

Returned products include `id` (UUID), `name`, `price`, and `stock`.

## Examples

Create a product:

```bash
curl -X POST http://localhost:8080/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Mouse","price":89.9,"stock":10}'
```

List products:

```bash
curl http://localhost:8080/products
```

Get a product:

```bash
curl http://localhost:8080/products/{id}
```

Delete a product:

```bash
curl -X DELETE http://localhost:8080/products/{id}
```
