package com.project.apt.view;

import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JRadioButtonFixture;
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
		window.textBox("nameTextBox").enterText("test1");
		window.textBox("quantityTextBox").enterText("10");
		window.button(JButtonMatcher.withName("addProductButton")).requireEnabled();
	}
	
	@Test
	public void testAddProductButtonShouldBeDisabledWhenAtLeastOneOfTheFieldsIsEmpty() {
		window.textBox("nameTextBox").enterText("test1");
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
			() -> productViewSwing.getListProductModel().addElement(new Product("test1", 10))
		);
		window.list("productList").selectItem(0);
		window.button(JButtonMatcher.withName("removeProductButton")).requireEnabled();
		window.list("productList").clearSelection();
		window.button(JButtonMatcher.withName("removeProductButton")).requireDisabled();
	}
	
	@Test
	public void testEditProductButtonShouldBeEnabledWhenEditFieldIsFilled() {
		window.textBox("editPropertiesTextBox").enterText("test2");
		window.button(JButtonMatcher.withName("editProductButton")).requireEnabled();
	}
	
	@Test
	public void testEditProductButtonShouldBeEnabledWhenIntegerIsEntered() {
		window.radioButton("quantityEditRadioButton").click();
		window.textBox("editPropertiesTextBox").enterText("10");
		window.button(JButtonMatcher.withName("editProductButton")).requireEnabled();
	}

}
