package com.project.apt.mvc;

import static org.assertj.core.api.Assertions.*;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testcontainers.containers.GenericContainer;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.project.apt.controller.ProductController;
import com.project.apt.model.Product;
import com.project.apt.repository.ProductRepositoryMongo;
import com.project.apt.view.ProductViewSwing;

@RunWith(GUITestRunner.class)
public class ModelViewControllerIT extends AssertJSwingJUnitTestCase {
	
	@SuppressWarnings("rawtypes")
	@ClassRule
	public static final GenericContainer mongo = new GenericContainer("mongo:4.2.3").withExposedPorts(27017);

	private FrameFixture window;
	
	private static final String testProductName1 = "test1";
	private static final String testProductName2 = "test2";
	private static final int testProductQuantity1 = 10;
	private static final int testProductQuantity2 = 5;
	
	private static final String DB_GESTIONALE = "gestionale";
	private static final String PRODUCT_COLLECTION = "product";
	
	private MongoClient client;
	private ProductRepositoryMongo productRepository;
	private ProductController productController;
	private ProductViewSwing productView;

	@Override
	protected void onSetUp() {
		client = new MongoClient(new ServerAddress(mongo.getContainerIpAddress(), mongo.getMappedPort(27017)));
		productRepository = new ProductRepositoryMongo(client, DB_GESTIONALE, PRODUCT_COLLECTION);
		for (Product product : productRepository.findAllProducts()) {
			productRepository.removeProduct(product);
		}
		GuiActionRunner.execute(() -> {
			productView = new ProductViewSwing();
			productController = new ProductController(productView, productRepository);
			productView.setProductController(productController);
			return productView;
		});
		window = new FrameFixture(robot(), productView);
		window.show();
	}
	
	@Override
	protected void onTearDown() {
		client.close();
	}
	
	@Test
	public void testAddProductShouldAddItToTheRepositoryAsWell() {
		window.textBox("nameTextBox").enterText(testProductName1);
		window.textBox("quantityTextBox").enterText(String.valueOf(testProductQuantity1));
		Product productToAdd = new Product(testProductName1, testProductQuantity1);
		window.button("addProductButton").click();
		assertThat(productRepository.findByName(testProductName1)).isEqualTo(productToAdd);
	}
	
	@Test
	public void testAddProductWhenProductWithSameNameIsPresentShouldUpdateQuantityOnTheRepositoryAsWell() {
		Product existingProduct = new Product(testProductName1, testProductQuantity1);
		GuiActionRunner.execute(
			() -> productController.addProduct(existingProduct)
		);
		window.textBox("nameTextBox").enterText(testProductName1);
		window.textBox("quantityTextBox").enterText(String.valueOf(testProductQuantity2));
		window.button("addProductButton").click();
		Product productWithUpdatedQuantity = new Product(testProductName1, testProductQuantity1 + testProductQuantity2);
		assertThat(productRepository.findByName(testProductName1)).isEqualTo(productWithUpdatedQuantity);
	}
	
	@Test
	public void testRemoveProductShouldRemoveItFromRepositoryAsWell() {
		Product productToRemove = new Product(testProductName1, testProductQuantity1);
		GuiActionRunner.execute(
			() -> productController.addProduct(productToRemove)
		);
		window.list("productList").selectItem(0);
		window.button("removeProductButton").click();
		assertThat(productRepository.findByName(testProductName1)).isNull();
	}
	
	@Test
	public void testEditProductWhenNameIsSelectedShouldChangeNameOnTheRepositoryAsWell() {
		Product productToUpdate = new Product(testProductName1, testProductQuantity1);
		GuiActionRunner.execute(
			() -> productController.addProduct(productToUpdate)
		);
		window.list("productList").selectItem(0);
		window.textBox("editPropertiesTextBox").enterText(testProductName2);
		window.radioButton("nameEditRadioButton").click();
		window.button("editProductButton").click();
		Product productWithUpdatedName = new Product(testProductName2, testProductQuantity1);
		assertThat(productRepository.findByName(testProductName2)).isEqualTo(productWithUpdatedName);
	}
	
	@Test
	public void testEditProductWhenQuantityIsSelectedShouldChangeQuantityOnTheRepositoryAsWell() {
		Product productToUpdate = new Product(testProductName1, testProductQuantity1);
		GuiActionRunner.execute(
			() -> productController.addProduct(productToUpdate)
		);
		window.list("productList").selectItem(0);
		window.textBox("editPropertiesTextBox").enterText(String.valueOf(testProductQuantity2));
		window.radioButton("quantityEditRadioButton").click();
		window.button("editProductButton").click();
		Product productWithUpdatedQuantity = new Product(testProductName1, testProductQuantity2);
		assertThat(productRepository.findByName(testProductName1)).isEqualTo(productWithUpdatedQuantity);
	}

}
