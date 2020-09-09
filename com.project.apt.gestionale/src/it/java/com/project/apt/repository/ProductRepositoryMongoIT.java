package com.project.apt.repository;

import static org.assertj.core.api.Assertions.*;

import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.GenericContainer;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.project.apt.model.Product;

public class ProductRepositoryMongoIT {

	private static final String testProductName1 = "test1";
	private static final String testProductName2 = "test2";
	private static final int testProductQuantity1 = 15;
	private static final int testProductQuantity2 = 5;

	private static final String DB_GESTIONALE = "gestionale";
	private static final String PRODUCT_COLLECTION = "product";
	
	@SuppressWarnings("rawtypes")
	@ClassRule
	public static final GenericContainer mongo = new GenericContainer("mongo:4.2.3").withExposedPorts(27017);
	
	private MongoClient client;
	private ProductRepositoryMongo productRepository;
	private MongoCollection<Document> productDocCollection;
	
	@Before
	public void setup() {
		client = new MongoClient(new ServerAddress(mongo.getContainerIpAddress(), mongo.getMappedPort(27017)));
		productRepository = new ProductRepositoryMongo(client, DB_GESTIONALE, PRODUCT_COLLECTION);
		MongoDatabase database = client.getDatabase(DB_GESTIONALE);
		database.drop();
		productDocCollection = database.getCollection(PRODUCT_COLLECTION);
	}
	
	@After
	public void tearDown() {
		client.close();
	}
	
	@Test
	public void testFindAllProducts() {
		Product product1 = addProductToDB(testProductName1, testProductQuantity1);
		Product product2 = addProductToDB(testProductName2, testProductQuantity2);
		assertThat(productRepository.findAllProducts()).containsExactly(product1, product2);
	}
	
	@Test
	public void testFindProductByName() {
		Product product1 = addProductToDB(testProductName1, testProductQuantity1);
		Product product2 = addProductToDB(testProductName2, testProductQuantity2);
		assertThat(productRepository.findByName(testProductName1)).isEqualTo(product1);
	}
	
	@Test
	public void testAddProductToDatabase() {
		Product productToAdd = new Product(testProductName1, testProductQuantity1);
		productRepository.addProduct(productToAdd);
		assertThat(productDocCollection.find()).hasSize(1).allMatch(
			d ->
			d.get("name").equals(productToAdd.getName()) &&
			d.get("quantity").equals(productToAdd.getQuantity())
		);
	}
	
	@Test
	public void testRemoveProductFromDatabase() {
		Product product1 = addProductToDB(testProductName1, testProductQuantity1);
		Product product2 = addProductToDB(testProductName2, testProductQuantity2);
		productRepository.removeProduct(product2);
		assertThat(productDocCollection.find()).hasSize(1).allMatch(
			d ->
			d.get("name").equals(product1.getName()) &&
			d.get("quantity").equals(product1.getQuantity())
		);
	}
	
	@Test
	public void testAlterProductName() {
		Product productToEdit = addProductToDB(testProductName1, testProductQuantity1);
		productRepository.alterProductName(productToEdit, testProductName2);
		assertThat(productDocCollection.find()).hasSize(1).allMatch(
			d ->
			d.get("name").equals(testProductName2) &&
			d.get("quantity").equals(productToEdit.getQuantity())
		);
	}
	
	@Test
	public void testAlterProductQuantity() {
		Product productToEdit = addProductToDB(testProductName1, testProductQuantity1);
		productRepository.alterProductQuantity(productToEdit, testProductQuantity2);
		assertThat(productDocCollection.find()).hasSize(1).allMatch(
			d ->
			d.get("name").equals(productToEdit.getName()) &&
			d.get("quantity").equals(testProductQuantity2)
		);
	}
	
	protected Product addProductToDB(String name, int quantity) {
		productDocCollection.insertOne(new Document().append("name", name).append("quantity", quantity));
		return new Product(name, quantity);
	}
}
