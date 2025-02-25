package com.backend.system.services.product;

import com.backend.system.models.domain.product.Product;
import com.backend.system.models.domain.user.User;
import com.backend.system.models.dto.ProductDTO;
import com.backend.system.models.mapper.ProductMapper;
import com.backend.system.repositories.ProductRepository;
import jakarta.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class ProductService {

    @Autowired
    ProductRepository repository;

    @Autowired
    ProductMapper mapper;

    public ProductDTO create(Product product) throws RuntimeException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof User user)) {
            throw new RuntimeException("User not authenticated.");
        }
        product.setUserId(user.getId());
        try {
            repository.save(product);
            return mapper.map(product);
        } catch (PersistenceException exception) {
            throw new RuntimeException("Error saving product: " + exception);
        } catch (Exception exception) {
            throw new RuntimeException("An unexpected error occurred: " + exception);
        }
    }
}