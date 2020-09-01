package com.project.apt.repository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.project.apt.model.Product;

public class ProductRepositoryMongo implements ProductRepository {

	private MongoCollection<Document> productDocCollection;

	public ProductRepositoryMongo(MongoClient client, String dbGestionale, String productCollection) {
		productDocCollection = client.getDatabase(dbGestionale).getCollection(productCollection);
	}

	@Override
	public List<Product> findAllProducts() {
		return StreamSupport.stream(productDocCollection.find().spliterator(), false).map(this::fromDocumentToProduct).collect(Collectors.toList());
	}

	@Override
	public Product findByName(String name) {
		Document productDoc = productDocCollection.find(Filters.eq("name", name)).first();
		if (productDoc == null) {
			return null;
		}
		return fromDocumentToProduct(productDoc);
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
	
	private Product fromDocumentToProduct(Document d) {
		return new Product("" + d.get("name"), (int) d.get("quantity"));
	}

}
