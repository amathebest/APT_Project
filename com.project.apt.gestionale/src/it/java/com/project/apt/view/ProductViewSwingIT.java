package com.project.apt.view;

import java.net.InetSocketAddress;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.InetSocketAddress;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

import com.project.apt.controller.ProductController;
import com.project.apt.model.Product;
import com.project.apt.repository.ProductRepositoryMongo;

@RunWith(GUITestRunner.class)
public class ProductViewSwingIT extends AssertJSwingJUnitTestCase {
	
	private static MongoServer server;
	private static InetSocketAddress serverAddress;
	
	private MongoClient mongoClient;
	
	private FrameFixture window;
	private ProductViewSwing productViewSwing;
	private ProductController productController;
	private ProductRepositoryMongo productRepositoryMongo;
	
	private static final String DB_GESTIONALE = "gestionale";
	private static final String PRODUCT_COLLECTION = "product";
	
	@BeforeClass
	public static void setupMongoServer() {
		server = new MongoServer(new MemoryBackend());
		serverAddress = server.bind();
	}
	
	@AfterClass
	public static void shutdownMongoServer() {
		server.shutdown();
	}
	
	@Override
	protected void onSetUp() {
		mongoClient = new MongoClient(new ServerAddress(serverAddress));
		productRepositoryMongo = new ProductRepositoryMongo(mongoClient, DB_GESTIONALE, PRODUCT_COLLECTION);
		for (Product product : productRepositoryMongo.findAllProducts()) {
			productRepositoryMongo.removeProduct(product);
		}
		GuiActionRunner.execute(() -> {
			productViewSwing = new ProductViewSwing();
			productController = new ProductController(productViewSwing, productRepositoryMongo);
			productViewSwing.setProductController(productController);
			return productViewSwing;
		});
		window = new FrameFixture(robot(), productViewSwing);
		window.show();
	}
	
	@Override
	protected void onTearDown() {
		mongoClient.close();
	}
	
	@Test
	public void test() {
		
	}

}
