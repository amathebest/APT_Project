package com.project.apt.view;

import java.util.List;

import com.project.apt.model.Product;

public interface ProductView {
	void listProducts(List<Product> products);
	void showError(String message, Product product, String type);
	void productAdded(Product product);
	void productEdited(Product product);
	void productDeleted(Product product);
}
