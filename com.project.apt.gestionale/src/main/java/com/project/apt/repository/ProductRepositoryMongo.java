package com.project.apt.repository;

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
		productDocCollection.insertOne(new Document().append("name", product.getName()).append("quantity", product.getQuantity()));
	}

	@Override
	public void removeProduct(Product product) {
		productDocCollection.deleteOne(Filters.eq("name", product.getName()));
		
	}

	@Override
	public void alterProductName(Product product, String newName) {
		Document docToUpdate = new Document().append("name", product.getName()).append("quantity", product.getQuantity());
		Document docToReplace = new Document().append("name", newName).append("quantity", product.getQuantity());
		productDocCollection.replaceOne(docToUpdate, docToReplace);
	}

	@Override
	public void alterProductQuantity(Product product, int newQuantity) {
		Document docToUpdate = new Document().append("name", product.getName()).append("quantity", product.getQuantity());
		Document docToReplace = new Document().append("name", product.getName()).append("quantity", newQuantity);
		productDocCollection.replaceOne(docToUpdate, docToReplace);
	}
	
	private Product fromDocumentToProduct(Document d) {
		return new Product("" + d.get("name"), (int) d.get("quantity"));
	}

}
