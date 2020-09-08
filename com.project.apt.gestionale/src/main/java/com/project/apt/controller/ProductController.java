package com.project.apt.controller;

import com.project.apt.model.Product;
import com.project.apt.repository.ProductRepository;
import com.project.apt.view.ProductView;

public class ProductController {
	
	private static final String NO_PRODUCT_FOUND_ERROR = "No such product existing in the database";
	private static final String ERROR_KEY = "error";
	private static final String INFO_KEY = "info";
	
	private ProductView productView;
	private ProductRepository productRepository;
	
	public ProductController(ProductView productView, ProductRepository productRepository) {
		this.productView = productView;
		this.productRepository = productRepository;
	}

	public void allProducts() {
		productView.listProducts(productRepository.findAllProducts());
	}
	
	public void addProduct(Product product) {
		Product existingProduct = productRepository.findByName(product.getName());
		if (existingProduct != null) {
			productRepository.alterProductQuantity(existingProduct, existingProduct.getQuantity() + product.getQuantity());
			Product updatedProduct = new Product(product.getName(), existingProduct.getQuantity() + product.getQuantity());
			productView.productEdited(existingProduct, updatedProduct);
			productView.showError("Updating quantity", updatedProduct, INFO_KEY);
			return;
		}
		productRepository.addProduct(product);
		productView.productAdded(product);
		productView.showError("Product added", product, INFO_KEY);
	}
	
	public void removeProduct(Product product) {
		Product existingProduct = productRepository.findByName(product.getName());
		if (existingProduct == null) {
			productView.showError(NO_PRODUCT_FOUND_ERROR, product, ERROR_KEY);
			return;
		}
		productRepository.removeProduct(product);
		productView.productDeleted(product);
		productView.showError("Product removed", product, INFO_KEY);
	}
	
	public void updateProductName(Product product, String newName) {
		Product existingProductOldName = productRepository.findByName(product.getName());
		Product existingProductNewName = productRepository.findByName(newName);
		if (existingProductOldName != null) {
			if (existingProductNewName == null) {
				productRepository.alterProductName(product, newName);
				Product updatedProduct = new Product(newName, product.getQuantity());
				productView.productEdited(product, updatedProduct);
				productView.showError("Changing product name", updatedProduct, INFO_KEY);
				return;
			}
			productView.showError("Database contains already a product with selected name", product, ERROR_KEY);
			return;
		}
		productView.showError(NO_PRODUCT_FOUND_ERROR, product, ERROR_KEY);
	}
	
	public void updateProductQuantity(Product product, int newQuantity) {
		Product existingProduct = productRepository.findByName(product.getName());
		if (existingProduct == null) {
			productView.showError(NO_PRODUCT_FOUND_ERROR, product, ERROR_KEY);
			return;
		}
		productRepository.alterProductQuantity(product, newQuantity);
		productView.productEdited(product, new Product(product.getName(), newQuantity));
		Product updatedProduct = new Product(product.getName(), newQuantity);
		productView.showError("Changing product quantity", updatedProduct, INFO_KEY);
	}
}
