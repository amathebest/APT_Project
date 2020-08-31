package com.project.apt.repository;

import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.project.apt.model.Product;
import com.project.apt.repository.ProductRepository;

public class ProductRepositoryMongo implements ProductRepository {

	private MongoCollection<Document> productDocCollection;

	public ProductRepositoryMongo(MongoClient client, String dbGestionale, String productCollection) {
		productDocCollection = client.getDatabase(dbGestionale).getCollection(productCollection);
	}

	@Override
	public List<Product> findAllProducts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Product findByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addProduct(Product product) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeProduct(Product product) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void alterProductName(Product product, String newName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void alterProductQuantity(Product product, int newQuantity) {
		// TODO Auto-generated method stub
		
	}

}
