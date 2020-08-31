package com.project.apt.controller;

import com.project.apt.model.Product;
import com.project.apt.repository.ProductRepository;
import com.project.apt.view.ProductView;

public class ProductController {
	
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
			productRepository.alterProductQuantity(existingProduct, product.getQuantity());
			productView.productEdited(existingProduct);
			productView.showError("Product already present, updating quantity instead.", existingProduct, "info");
			return;
		}
		productRepository.addProduct(product);
		productView.productAdded(product);
	}
	
	public void removeProduct(Product product) {
		Product existingProduct = productRepository.findByName(product.getName());
		if (existingProduct == null) {
			productView.showError("No such product existing in the database.", product, "error");
			return;
		}
		productRepository.removeProduct(product);
		productView.productDeleted(product);
	}
	
	public void updateProductName(Product product, String newName) {
		Product existingProduct = productRepository.findByName(product.getName());
		if (existingProduct == null) {
			productView.showError("No such product existing in the database.", product, "error");
			return;
		}
		productRepository.alterProductName(product, newName);
		productView.productEdited(product);
	}
	
	public void updateProductQuantity(Product product, int newQuantity) {
		Product existingProduct = productRepository.findByName(product.getName());
		if (existingProduct == null) {
			productView.showError("No such product existing in the database.", product, "error");
			return;
		}
		productRepository.alterProductQuantity(product, newQuantity);
		productView.productEdited(product);
	}
}
