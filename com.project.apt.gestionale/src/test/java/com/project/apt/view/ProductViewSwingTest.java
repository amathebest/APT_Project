package com.project.apt.view;

import static org.assertj.core.api.Assertions.*;

import java.awt.Color;
import java.util.Arrays;

import javax.swing.DefaultListModel;

import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.project.apt.controller.ProductController;
import com.project.apt.model.Product;

@RunWith(GUITestRunner.class)
public class ProductViewSwingTest extends AssertJSwingJUnitTestCase {
	
	private static final String testProductName1 = "test1";
	private static final String testProductName2 = "test2";
	private static final int testProductQuantity1 = 10;
	private static final int testProductQuantity2 = 5;
	
	private FrameFixture window;
	
	private ProductViewSwing productViewSwing;
	
	@Mock
	private ProductController productController;

	@Override
	protected void onSetUp() {
		MockitoAnnotations.initMocks(this);
		GuiActionRunner.execute(() -> {
			productViewSwing = new ProductViewSwing();
			productViewSwing.setProductController(productController);
			return productViewSwing;
		});
		window = new FrameFixture(robot(), productViewSwing);
		window.show();
	}
	
	@Test
	public void testControlsInitialState() {
		// left side
		window.list("productList");
		// right side
		window.label(JLabelMatcher.withName("lblAddNewProduct"));
		window.textBox("nameTextBox").requireEnabled();
		window.textBox("quantityTextBox").requireEnabled();
		window.button(JButtonMatcher.withName("removeProductButton")).requireDisabled();
		window.button(JButtonMatcher.withName("addProductButton")).requireDisabled();
		window.label(JLabelMatcher.withName("lblEditExistingProduct"));
		window.textBox("editPropertiesTextBox").requireEnabled();
		window.radioButton("nameEditRadioButton").requireEnabled();
		window.radioButton("quantityEditRadioButton").requireEnabled();
		window.button(JButtonMatcher.withName("editProductButton")).requireDisabled();
		window.label(JLabelMatcher.withName("lblInfoMessages"));
		window.label(JLabelMatcher.withName("lblMessage"));
		
	}
	
	@Test
	public void testAddProductButtonShouldBeEnabledWhenBothFieldsGetFilled() {
		window.textBox("nameTextBox").enterText(testProductName1);
		window.textBox("quantityTextBox").enterText("10");
		window.button(JButtonMatcher.withName("addProductButton")).requireEnabled();
	}
	
	@Test
	public void testAddProductButtonShouldBeDisabledWhenAtLeastOneOfTheFieldsIsEmpty() {
		window.textBox("nameTextBox").enterText(testProductName1);
		window.textBox("quantityTextBox").enterText(" ");
		window.button(JButtonMatcher.withName("addProductButton")).requireDisabled();

		window.textBox("nameTextBox").setText("");
		window.textBox("quantityTextBox").setText("");
		
		window.textBox("nameTextBox").enterText(" ");
		window.textBox("quantityTextBox").enterText("10");
		window.button(JButtonMatcher.withName("addProductButton")).requireDisabled();
	}
	
	@Test
	public void testRemoveProductButtonShouldBeEnabledWhenOneProductIsSelectedOnTheList() {
		GuiActionRunner.execute(
			() -> productViewSwing.getListProductModel().addElement(new Product(testProductName1, testProductQuantity1))
		);
		window.list("productList").selectItem(0);
		window.button(JButtonMatcher.withName("removeProductButton")).requireEnabled();
		window.list("productList").clearSelection();
		window.button(JButtonMatcher.withName("removeProductButton")).requireDisabled();
	}
	
	@Test
	public void testRemoveProductButtonShouldBeDisabledWhenNoProductIsSelected() {
		GuiActionRunner.execute(
			() -> productViewSwing.getListProductModel().addElement(new Product(testProductName1, testProductQuantity1))
		);
		window.button(JButtonMatcher.withName("removeProductButton")).requireDisabled();
	}
	
	@Test
	public void testEditProductButtonShouldBeEnabledWhenOneProductIsSelectedAndEditFieldIsFilled() {
		GuiActionRunner.execute(
			() -> productViewSwing.getListProductModel().addElement(new Product(testProductName1, testProductQuantity1))
		);
		window.list("productList").selectItem(0);
		window.textBox("editPropertiesTextBox").enterText(testProductName2);
		window.button(JButtonMatcher.withName("editProductButton")).requireEnabled();
	}
	
	@Test
	public void testEditProductButtonShouldBeEnabledWhenOneProductIsSelectedAndIntegerIsEntered() {
		GuiActionRunner.execute(
			() -> productViewSwing.getListProductModel().addElement(new Product(testProductName1, testProductQuantity1))
		);
		window.list("productList").selectItem(0);
		window.radioButton("quantityEditRadioButton").click();
		window.textBox("editPropertiesTextBox").enterText("5");
		window.button(JButtonMatcher.withName("editProductButton")).requireEnabled();
	}
	
	@Test
	public void testEditProductButtonShouldBeDisabledWhenFieldIsFilledButNoProductIsSelected() {
		GuiActionRunner.execute(
			() -> productViewSwing.getListProductModel().addElement(new Product(testProductName1, testProductQuantity1))
		);
		window.textBox("editPropertiesTextBox").enterText("5");
		window.button(JButtonMatcher.withName("editProductButton")).requireDisabled();
	}
	
	@Test
	public void testEditProductButtonShouldBeDisabledWhenProductIsSelectedButFieldIsEmpty() {
		GuiActionRunner.execute(
			() -> productViewSwing.getListProductModel().addElement(new Product(testProductName1, testProductQuantity1))
		);
		window.list("productList").selectItem(0);
		window.textBox("editPropertiesTextBox").setText("");
		window.button(JButtonMatcher.withName("editProductButton")).requireDisabled();
	}
	
	@Test
	public void testListProductsShouldDisplayAllProductsInTheProductList() {
		Product product1 = new Product(testProductName1, testProductQuantity1);
		Product product2 = new Product(testProductName2, testProductQuantity2);
		GuiActionRunner.execute(
			() -> productViewSwing.listProducts(Arrays.asList(product1, product2))
		);
		String[] listProducts = window.list().contents();
		assertThat(listProducts).containsExactly(product1.toString(), product2.toString());
	}
	
	@Test
	public void testShowErrorShouldShowInfoOnTheDedicatedErrorLabel() {
		Product product1 = new Product(testProductName1, testProductQuantity1);
		GuiActionRunner.execute(
			() -> productViewSwing.showError("Error message", product1, "error")
		);
		window.label("lblMessage").requireText("Error message: " + product1);
	}
	
	@Test
	public void testShowErrorShowsInfoInRedIfMessageIsError() {
		Product product1 = new Product(testProductName1, testProductQuantity1);
		GuiActionRunner.execute(
			() -> productViewSwing.showError("Error message", product1, "error")
		);
		window.label("lblMessage").requireText("Error message: " + product1);
		window.label("lblMessage").foreground().requireEqualTo(Color.RED);
	}
	
	@Test
	public void testShowErrorShowsInfoInBlackIfMessageIsInfo() {
		Product product1 = new Product(testProductName1, testProductQuantity1);
		GuiActionRunner.execute(
			() -> productViewSwing.showError("Error message", product1, "info")
		);
		window.label("lblMessage").requireText("Error message: " + product1);
		window.label("lblMessage").foreground().requireEqualTo(Color.BLACK);
	}
	
	@Test
	public void testProductAddedShouldAddProductToTheListAndCleanupErrorMessage() {
		Product product1 = new Product(testProductName1, testProductQuantity1);
		GuiActionRunner.execute(
			() -> productViewSwing.productAdded(product1)
		);
		String[] listProducts = window.list().contents();
		assertThat(listProducts).containsExactly(product1.toString());
		window.label("lblMessage").requireText(" ");
	}
	
	@Test
	public void testProductDeletedShouldRemoveProductFromTheListAndCleanupErrorMessage() {
		Product product1 = new Product(testProductName1, testProductQuantity1);
		Product product2 = new Product(testProductName2, testProductQuantity2);
		GuiActionRunner.execute(
			() -> {
				DefaultListModel<Product> listProductModel = productViewSwing.getListProductModel();
				listProductModel.addElement(product1);
				listProductModel.addElement(product2);
			}
		);
		GuiActionRunner.execute(
			() -> productViewSwing.productDeleted(product2)
		);
		String[] listProducts = window.list().contents();
		assertThat(listProducts).containsExactly(product1.toString());
		window.label("lblMessage").requireText(" ");		
	}
	
	@Test
	public void testProductEditedShouldEditProductOnTheListAndCleanupErrorMessage() {
		Product product1 = new Product(testProductName1, testProductQuantity1);
		Product product2 = new Product(testProductName2, testProductQuantity1);
		GuiActionRunner.execute(
			() -> {
				DefaultListModel<Product> listProductModel = productViewSwing.getListProductModel();
				listProductModel.addElement(product1);
			}
		);
		GuiActionRunner.execute(
			() -> productViewSwing.productEdited(product1, product2)
		);
		String[] listProducts = window.list().contents();
		assertThat(listProducts).containsExactly(product2.toString());
		window.label("lblMessage").requireText(" ");
	}
}
