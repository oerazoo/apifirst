package org.acme;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import org.acme.domain.ErrorApi;
import org.acme.domain.NewProduct;
import org.acme.domain.Product;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class ProductsResource implements ProductsApi{

    private final Map<UUID, Product> db = new ConcurrentHashMap<>();

    @Override
    public Response createProduct(NewProduct newProduct) {

        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setName(newProduct.getName());
        product.setPrice(newProduct.getPrice());
        product.setStock(newProduct.getStock() == null ? 0 : newProduct.getStock());
        db.put(product.getId(), product);

        return Response.status(Response.Status.CREATED).entity(product).build();
    }

    @Override
    public Response deleteProduct(UUID id) {
        return db.remove(id) == null
                ? notFound(id)
                : Response.noContent().build();
    }

    @Override
    public Response getProduct(UUID id) {
        Product product = db.get(id);
        return product == null ? notFound(id) : Response.ok(product).build();
    }

    @Override
    public Response listProducts() {
        return Response.ok(new ArrayList<>(db.values())).build();
    }

    private Response notFound(UUID id){
        ErrorApi error = new ErrorApi();
        error.setCode("PRODUCT_NOT_FOUND");
        error.setMessage(String.format("Product not found: %s", id));
        return Response.status(Response.Status.NOT_FOUND).entity(error).build();
    }
}
