package com.project.apt.repository;

import static org.assertj.core.api.Assertions.*;

import java.net.InetSocketAddress;

import org.bson.Document;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.project.apt.model.Product;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

public class ProductRepositoryMongoTest {
	
	private static final String testProductName1 = "test1";
	private static final String testProductName2 = "test2";
	private static final int testProductQuantity1 = 10;
	private static final int testProductQuantity2 = 5;

	private static MongoServer server;
	private static InetSocketAddress serverAddress;
	private MongoClient client;
	private ProductRepositoryMongo productRepository;
	private MongoCollection<Document> productDocCollection;
	

	private static final String DB_GESTIONALE = "gestionale";
	private static final String PRODUCT_COLLECTION = "product";

	
	@BeforeClass
	public static void setupServer() {
		server = new MongoServer(new MemoryBackend());
		serverAddress = server.bind();
	}

	@AfterClass
	public static void shutdownServer() {
		server.shutdown();
	}

	@Before
	public void setup() {
		client = new MongoClient(new ServerAddress(serverAddress));
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
	public void testFindAllWhenDBIsEmptyReturnsAnEmptyList() {
		assertThat(productRepository.findAllProducts()).isEmpty();
	}
	
	@Test
	public void testFindAllWhenDBIsNotEmptyReturnsAListOfTheProducts() {
		productDocCollection.insertOne(new Document().append("name", testProductName1).append("quantity", testProductQuantity1));
		productDocCollection.insertOne(new Document().append("name", testProductName2).append("quantity", testProductQuantity2));
		Product firstProduct = new Product(testProductName1, testProductQuantity1);
		Product secondProduct = new Product(testProductName2, testProductQuantity2);
		assertThat(productRepository.findAllProducts()).containsExactly(firstProduct, secondProduct);
	}
	
	@Test
	public void testFindByNameWhenNoProductWithSpecifiedNameIsFound() {
		assertThat(productRepository.findByName(testProductName1)).isNull();
	}
	
	@Test
	public void testFindByNameWhenProductWithSpecifiedNameExistsInDB() {
		productDocCollection.insertOne(new Document().append("name", testProductName1).append("quantity", testProductQuantity1));
		Product existingProduct = new Product(testProductName1, testProductQuantity1);
		assertThat(productRepository.findByName(testProductName1)).isEqualTo(existingProduct);
	}
	
	@Test
	public void testAddProductShouldCreateNewDocument() {
		Product productToSave = new Product(testProductName1, testProductQuantity1);
		productRepository.addProduct(productToSave);
		assertThat(productDocCollection.find()).hasSize(1).allMatch(
			d ->
			d.get("name").equals(productToSave.getName()) &&
			d.get("quantity").equals(productToSave.getQuantity())
		);
	}
	
	@Test
	public void testRemoveProductShouldRemoveProduct() {
		productDocCollection.insertOne(new Document().append("name", testProductName1).append("quantity", testProductQuantity1));
		productDocCollection.insertOne(new Document().append("name", testProductName2).append("quantity", testProductQuantity2));
		Product firstProduct = new Product(testProductName1, testProductQuantity1);
		Product secondProduct = new Product(testProductName2, testProductQuantity2);
		productRepository.removeProduct(secondProduct);
		assertThat(productDocCollection.find()).hasSize(1).allMatch(
			d ->
			d.get("name").equals(firstProduct.getName()) &&
			d.get("quantity").equals(firstProduct.getQuantity())
		);
	}
	
	@Test
	public void testAlterProductNameShouldChangeProductsName() {
		productDocCollection.insertOne(new Document().append("name", testProductName1).append("quantity", testProductQuantity1));
		Product productToUpdate = new Product(testProductName1, testProductQuantity1);
		productRepository.alterProductName(productToUpdate, testProductName2);
		assertThat(productDocCollection.find()).hasSize(1).allMatch(
			d ->
			d.get("name").equals(testProductName2) &&
			d.get("quantity").equals(productToUpdate.getQuantity())
		);	
	}
	
	public void testAlterProductQuantityShouldChangeProductQuantity() {
		productDocCollection.insertOne(new Document().append("name", testProductName1).append("quantity", testProductQuantity1));
		Product productToUpdate = new Product(testProductName1, testProductQuantity1);
		productRepository.alterProductQuantity(productToUpdate, testProductQuantity2);
		assertThat(productDocCollection.find()).hasSize(1).allMatch(
			d ->
			d.get("name").equals(productToUpdate.getName()) &&
			d.get("quantity").equals(testProductQuantity2)
		);	
	}
	
	
	
	
	
	
}
