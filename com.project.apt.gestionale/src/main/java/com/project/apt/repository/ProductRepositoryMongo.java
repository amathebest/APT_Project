package com.project.apt.repository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.project.apt.model.Product;

public class ProductRepositoryMongo implements ProductRepository {

	private MongoCollection<Document> productDocCollection;
	
	private static final String NAME_KEY = "name";
	private static final String QUANTITY_KEY = "quantity";
	
	public ProductRepositoryMongo(MongoClient client, String dbGestionale, String productCollection) {
		productDocCollection = client.getDatabase(dbGestionale).getCollection(productCollection);
	}

	@Override
	public List<Product> findAllProducts() {
		return StreamSupport.stream(productDocCollection.find().spliterator(), false).map(this::fromDocumentToProduct).collect(Collectors.toList());
	}

	@Override
	public Product findByName(String name) {
		Document productDoc = productDocCollection.find(Filters.eq(NAME_KEY, name)).first();
		if (Objects.nonNull(productDoc)) {
			return fromDocumentToProduct(productDoc);
		}
		return null;
	}

	@Override
	public void addProduct(Product product) {
		productDocCollection.insertOne(new Document().append(NAME_KEY, product.getName()).append(QUANTITY_KEY, product.getQuantity()));
	}

	@Override
	public void removeProduct(Product product) {
		productDocCollection.deleteOne(Filters.eq(NAME_KEY, product.getName()));
	}

	@Override
	public void alterProductName(Product product, String newName) {
		Document docToUpdate = new Document().append(NAME_KEY, product.getName()).append(QUANTITY_KEY, product.getQuantity());
		Document docToReplace = new Document().append(NAME_KEY, newName).append(QUANTITY_KEY, product.getQuantity());
		productDocCollection.replaceOne(docToUpdate, docToReplace);
	}

	@Override
	public void alterProductQuantity(Product product, int newQuantity) {
		Document docToUpdate = new Document().append(NAME_KEY, product.getName()).append(QUANTITY_KEY, product.getQuantity());
		Document docToReplace = new Document().append(NAME_KEY, product.getName()).append(QUANTITY_KEY, newQuantity);
		productDocCollection.replaceOne(docToUpdate, docToReplace);
	}
	
	private Product fromDocumentToProduct(Document doc) {
		return new Product("" + doc.get(NAME_KEY), (int) doc.get(QUANTITY_KEY));
	}

}
