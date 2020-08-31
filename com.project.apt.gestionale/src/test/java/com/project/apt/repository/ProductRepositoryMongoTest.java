package com.project.apt.repository;

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

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

public class ProductRepositoryMongoTest {

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
	public void test() {
		
	}

}
