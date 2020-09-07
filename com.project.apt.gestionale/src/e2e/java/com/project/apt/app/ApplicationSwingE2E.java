package com.project.apt.app;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.swing.launcher.ApplicationLauncher.*;

import java.util.regex.Pattern;

import javax.swing.JFrame;

import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.GuiActionRunner;
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
import com.mongodb.client.model.Filters;
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
	private static final String testProductName3 = "test3";
	private static final int testProductQuantity1 = 10;
	private static final int testProductQuantity2 = 5;
	private static final int testProductQuantity3 = 15;
	
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
	
	protected void removeProductToDatabaseForTesting(String name) {
		client.getDatabase(DB_GESTIONALE).getCollection(PRODUCT_COLLECTION).deleteOne(Filters.eq("name", name));
	}
	
	@Test
	public void testShowAllElementsOnDatabaseAtTheStartOfTheApplication() {
		String[] listProducts = window.list().contents();
		Product product1 = new Product(testProductName1, testProductQuantity1);
		Product product2 = new Product(testProductName2, testProductQuantity2);
		
		assertThat(listProducts).contains(product1.toString());
		assertThat(listProducts).contains(product2.toString());
	}
	
	@Test
	public void testAddProductButtonWhenNoProductWithSameNameIsPresent() {
		Product productToAdd = new Product(testProductName3, testProductQuantity3);
		
		window.textBox("nameTextBox").enterText(productToAdd.getName());
		window.textBox("quantityTextBox").enterText(String.valueOf(productToAdd.getQuantity()));
		window.button("addProductButton").click();

		String[] listProducts = window.list().contents();
		assertThat(listProducts).contains(productToAdd.toString());
	}
	
	@Test
	public void testAddProductButtonWhenProductWithSameNameIsPresent() {
		Product productToAdd = new Product(testProductName1, testProductQuantity3);
		
		window.textBox("nameTextBox").enterText(productToAdd.getName());
		window.textBox("quantityTextBox").enterText(String.valueOf(productToAdd.getQuantity()));
		window.button("addProductButton").click();
		
		String errorMessage = window.label("lblMessage").text();
		assertThat(errorMessage).contains(productToAdd.getName());
	}
	
	@Test
	public void testRemoveProductButtonWhenProductSelectedExistsInDB() {
		Product productToRemove = new Product(testProductName1, testProductQuantity1);
		
		window.list("productList").selectItem(Pattern.compile(".*" + testProductName1 + ".*"));
		window.button("removeProductButton").click();
		
		String[] listProducts = window.list().contents();
		assertThat(listProducts).doesNotContain(productToRemove.toString());
	}
	
	@Test
	public void testRemoveProductButtonWhenProductDoesNotExistInDB() {
		Product productToRemove = new Product(testProductName1, testProductQuantity1);

		window.list("productList").selectItem(Pattern.compile(".*" + testProductName1 + ".*"));
		removeProductToDatabaseForTesting(productToRemove.getName());
		window.button("removeProductButton").click();

		String errorMessage = window.label("lblMessage").text();
		assertThat(errorMessage).contains(productToRemove.getName());
	}
	
	@Test
	public void testEditProductButtonWhenNameIsSelectedProductIsSelectedAndFieldIsFilled() {
		Product productToUpdate = new Product(testProductName1, testProductQuantity1);

		window.radioButton("nameEditRadioButton").click();
		window.list("productList").selectItem(Pattern.compile(".*" + testProductName1 + ".*"));
		window.textBox("editPropertiesTextBox").enterText(testProductName3);
		window.button("editProductButton").click();
		
		Product updatedProduct = new Product(testProductName3, productToUpdate.getQuantity());
		String[] listProducts = window.list().contents();
		assertThat(listProducts).contains(updatedProduct.toString());
	}
	
	@Test
	public void testEditProductButtonWhenQuantityIsSelectedProductIsSelectedAndFieldIsFilledWithInteger() {
		Product productToUpdate = new Product(testProductName1, testProductQuantity1);

		window.radioButton("quantityEditRadioButton").click();
		window.list("productList").selectItem(Pattern.compile(".*" + testProductName1 + ".*"));
		window.textBox("editPropertiesTextBox").enterText(String.valueOf(testProductQuantity3));
		window.button("editProductButton").click();
		
		Product updatedProduct = new Product(productToUpdate.getName(), testProductQuantity3);
		String[] listProducts = window.list().contents();
		assertThat(listProducts).contains(updatedProduct.toString());
	}
	
	@Test
	public void testEditProductButtonWhenProductDoesNotExistInDB() {
		Product productToUpdate = new Product(testProductName1, testProductQuantity1);

		window.list("productList").selectItem(Pattern.compile(".*" + testProductName1 + ".*"));
		window.radioButton("nameEditRadioButton").click();
		window.textBox("editPropertiesTextBox").enterText(testProductName3);
		removeProductToDatabaseForTesting(productToUpdate.getName());
		window.button("editProductButton").click();

		String errorMessage = window.label("lblMessage").text();
		assertThat(errorMessage).contains(productToUpdate.getName());
	}
	
}
