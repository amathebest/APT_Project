package com.project.apt.app;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.swing.launcher.ApplicationLauncher.*;

import javax.swing.JFrame;

import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.bson.Document;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testcontainers.containers.GenericContainer;

import com.mongodb.MongoClient;
import com.project.apt.model.Product;

@RunWith(GUITestRunner.class)
public class ApplicationSwingE2E extends AssertJSwingJUnitTestCase {

	@SuppressWarnings("rawtypes")
	@ClassRule
	public static final GenericContainer mongo = new GenericContainer("mongo:4.2.3").withExposedPorts(27017);
	
	private MongoClient client;
	private FrameFixture window;

	private static final String testProductName1 = "test1";
	private static final String testProductName2 = "test2";
	private static final int testProductQuantity1 = 10;
	private static final int testProductQuantity2 = 5;
	
	private static final String DB_GESTIONALE = "gestionale";
	private static final String PRODUCT_COLLECTION = "product";
	
	@Override
	protected void onSetUp() {
		String containerIpAddress = mongo.getContainerIpAddress();
		Integer mappedPort = mongo.getMappedPort(27017);
		client = new MongoClient(containerIpAddress, mappedPort);
		client.getDatabase(DB_GESTIONALE).drop();
		addProductToDatabaseForTesting(testProductName1, testProductQuantity1);
		addProductToDatabaseForTesting(testProductName2, testProductQuantity2);
		
		application("com.project.apt.app.ApplicationSwing").withArgs(
			"--mongo-host=" + containerIpAddress, 
			"--mongo-port=" + mappedPort.toString(), 
			"--db-name=" + DB_GESTIONALE, 
			"--db-collection=" + PRODUCT_COLLECTION
		).start();
		window = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
			@Override
			protected boolean isMatching(JFrame frame) {
				return "Product Manager".equals(frame.getTitle()) && frame.isShowing();
			}
		}).using(robot());
	}
	
	@Override
	protected void onTearDown() {
		client.close();
	}
	
	protected void addProductToDatabaseForTesting(String name, int quantity) {
		Document productDocument = new Document().append("name", name).append("quantity", quantity);
		client.getDatabase(DB_GESTIONALE).getCollection(PRODUCT_COLLECTION).insertOne(productDocument);
	}
	
	@Test
	public void testShowAllElementsOnDatabaseAtTheStartOfTheApplication() {
		String[] listProducts = window.list().contents();
		Product product1 = new Product(testProductName1, testProductQuantity1);
		Product product2 = new Product(testProductName2, testProductQuantity2);
		assertThat(listProducts).contains(product1.toString());
		assertThat(listProducts).contains(product2.toString());
	}
	
	
	
	
	
	
}
