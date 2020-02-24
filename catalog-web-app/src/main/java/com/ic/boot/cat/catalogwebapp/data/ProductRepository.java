package com.ic.boot.cat.catalogwebapp.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Collection;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
    Collection<Product> findProductsByCatalog(@Param("name")String name);
    void deleteProductsByCatalog(@Param("name")String name);
}
