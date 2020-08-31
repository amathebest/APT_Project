package com.project.apt.repository;

import java.util.List;

import com.project.apt.model.Product;

public interface ProductRepository {
	public List<Product> findAllProducts();
	public Product findByName(String name);
	public void addProduct(Product product);
	public void removeProduct(Product product);
	public void alterProductName(Product product, String newName);
	public void alterProductQuantity(Product product, int newQuantity);
}
