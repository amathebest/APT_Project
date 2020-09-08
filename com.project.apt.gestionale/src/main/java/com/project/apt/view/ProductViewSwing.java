package com.project.apt.view;

import java.util.List;

import javax.swing.JFrame;

import com.project.apt.controller.ProductController;
import com.project.apt.model.Product;
import com.project.apt.view.utils.MyGridBagConstraints;

import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;

public class ProductViewSwing extends JFrame implements ProductView {
	
	private static final long serialVersionUID = 1L;
	
	private transient ProductController productController;

	private JList<Product> listProduct;
	private DefaultListModel<Product> listProductModel;
	
	private JTextField txtInsertProductName;
	private JTextField txtInsertProductQuantity;
	private JTextField txtEditProductProperties;

	private JButton removeProductButton;
	private JButton addProductButton;
	private JButton editProductButton;
	
	private JLabel lblMessage;
	
	private JRadioButton quantityButton;
	private JRadioButton nameButton;
	
	public void setProductController(ProductController productController) {
		this.productController = productController;
	}
	
	public ProductViewSwing() {
		setMinimumSize(new Dimension(600, 400));
		setTitle("Product Manager");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		getContentPane().setLayout(new BorderLayout());
		
		JPanel mainContent = new JPanel();
		mainContent.setLayout(new GridLayout(0, 2));
		mainContent.add(createLeftPanel());
		mainContent.add(createRightPanel());
		getContentPane().add(mainContent);

		pack();
        setVisible(true);
	}
	
	protected JPanel createLeftPanel() {
        JPanel panel = new JPanel(new BorderLayout());
    	JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane);
		
		// Listeners
		
		ListSelectionListener removeProductButtonEnabler = new ListSelectionListener() {
    		@Override
    		public void valueChanged(ListSelectionEvent e) {
    			removeProductButton.setEnabled(
    				listProduct.getSelectedIndex() != -1
    			);
    		}
    	};
		
		// Components
		
		listProductModel = new DefaultListModel<>();
		listProduct = new JList<>(listProductModel);
		listProduct.setMinimumSize(new Dimension(250, 300));
		listProduct.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listProduct.setName("productList");
		listProduct.addListSelectionListener(removeProductButtonEnabler);
		scrollPane.setViewportView(listProduct);
        
		return panel;
	}
	
	protected JPanel createRightPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        GridBagLayout gblContent = new GridBagLayout();
        gblContent.columnWeights = new double[]{1.0};
        JPanel content = new JPanel(gblContent);
        panel.add(content);
        
        // Listeners
        
        KeyAdapter addProductButtonEnabler = new KeyAdapter() {
    		@Override
    		public void keyReleased(KeyEvent e) {
    			addProductButton.setEnabled(
    				!txtInsertProductName.getText().trim().isEmpty() && 
    				!txtInsertProductQuantity.getText().trim().isEmpty() && 
    				txtInsertProductQuantity.getText().matches("^[1-9][0-9]*")
    			);
    		}
    	};
    	
    	KeyAdapter editProductButtonEnabler = new KeyAdapter() {
    		@Override
    		public void keyReleased(KeyEvent e) {
    			editProductButton.setEnabled(
    				listProduct.getSelectedIndex() != -1 && 
    				((quantityButton.isSelected() && txtEditProductProperties.getText().matches("^[1-9][0-9]*")) || 
    				  nameButton.isSelected() && txtEditProductProperties.getText().matches("^[a-zA-Z]+[0-9]*"))
    			);
    		}
    	};
    	
    	ActionListener addProductControllerDelegation = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				productController.addProduct(new Product(txtInsertProductName.getText(), Integer.parseInt(txtInsertProductQuantity.getText())));
			}
    	};
    	
    	ActionListener removeProductControllerDelegation = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				productController.removeProduct(listProduct.getSelectedValue());
			}
    	};
    	
    	ActionListener editProductControllerDelegation = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (nameButton.isSelected()) {
					productController.updateProductName(listProduct.getSelectedValue(), txtEditProductProperties.getText());
				} else {
					productController.updateProductQuantity(listProduct.getSelectedValue(), Integer.parseInt(txtEditProductProperties.getText()));
				}
			}
    	};
    	
    	// Components 
    	
    	JLabel lblAddNewProduct = new JLabel("Add new product");
    	lblAddNewProduct.setName("lblAddNewProduct");
		GridBagConstraints gbclblAddNewProduct = new MyGridBagConstraints(0, 0);
		gbclblAddNewProduct.anchor = GridBagConstraints.WEST;
		content.add(lblAddNewProduct, gbclblAddNewProduct);
    	
        txtInsertProductName = new JTextField();
        txtInsertProductName.setName("nameTextBox");
        txtInsertProductName.addKeyListener(addProductButtonEnabler);
        GridBagConstraints gbcTxtInsertProductName = new MyGridBagConstraints(0, 1);
        content.add(txtInsertProductName, gbcTxtInsertProductName);
        txtInsertProductName.setColumns(25);
        
        
        txtInsertProductQuantity = new JTextField();
        txtInsertProductQuantity.setName("quantityTextBox");
        txtInsertProductQuantity.addKeyListener(addProductButtonEnabler);
        GridBagConstraints gbcTxtInsertProductQuantity = new MyGridBagConstraints(0, 2);
        content.add(txtInsertProductQuantity, gbcTxtInsertProductQuantity);
        txtInsertProductQuantity.setColumns(25);
        
        
        JPanel buttonsSplit = new JPanel(new GridLayout(0, 2));
        GridBagConstraints gbcButtonsSplit = new MyGridBagConstraints(0, 3);

		removeProductButton = new JButton("Remove");
		removeProductButton.setName("removeProductButton");
		removeProductButton.setEnabled(false);
		removeProductButton.addActionListener(removeProductControllerDelegation);
		buttonsSplit.add(removeProductButton);

		addProductButton = new JButton("Add");
		addProductButton.setName("addProductButton");
		addProductButton.setEnabled(false);
		addProductButton.addActionListener(addProductControllerDelegation);
		buttonsSplit.add(addProductButton);
		
		content.add(buttonsSplit, gbcButtonsSplit);
		
		JLabel lblEditExistingProduct = new JLabel("Edit existing product");
		lblEditExistingProduct.setName("lblEditExistingProduct");
		GridBagConstraints gbcLblEditExistingProduct = new MyGridBagConstraints(0, 4);
		gbcLblEditExistingProduct.anchor = GridBagConstraints.WEST;
		content.add(lblEditExistingProduct, gbcLblEditExistingProduct);
		
        txtEditProductProperties = new JTextField();
        txtEditProductProperties.setName("editPropertiesTextBox");
        txtEditProductProperties.addKeyListener(editProductButtonEnabler);
        GridBagConstraints gbcTxtEditProductProperties = new MyGridBagConstraints(0, 5);
        content.add(txtEditProductProperties, gbcTxtEditProductProperties);
        txtEditProductProperties.setColumns(25);
        
        
        JPanel editSplit = new JPanel(new GridLayout(0, 2));
        GridBagConstraints gbcEditSplit = new MyGridBagConstraints(0, 6);

        String nameString = "Name";
        nameButton = new JRadioButton(nameString);
        nameButton.setName("nameEditRadioButton");
        nameButton.setMnemonic(KeyEvent.VK_B);
        nameButton.setActionCommand(nameString);
        nameButton.setSelected(true);
        
        String quantityString = "Quantity";
        quantityButton = new JRadioButton(quantityString);
        quantityButton.setName("quantityEditRadioButton");
        quantityButton.setMnemonic(KeyEvent.VK_B);
        quantityButton.setActionCommand(quantityString);
        
        ButtonGroup editChoice = new ButtonGroup();
        editChoice.add(nameButton);
        editChoice.add(quantityButton);
        
        JPanel editButtonGroupSplit = new JPanel(new GridLayout(2, 0));
        editButtonGroupSplit.add(nameButton);
        editButtonGroupSplit.add(quantityButton);
        
        editSplit.add(editButtonGroupSplit);
        
		editProductButton = new JButton("Edit");
		editProductButton.setName("editProductButton");
		editProductButton.setEnabled(false);
		editProductButton.addActionListener(editProductControllerDelegation);
		editSplit.add(editProductButton);
		
		content.add(editSplit, gbcEditSplit);
		
		JLabel lblInfoMessages = new JLabel("Info messages");
		lblInfoMessages.setName("lblInfoMessages");
		GridBagConstraints gbcLblInfoMessages = new MyGridBagConstraints(0, 7);
		gbcLblInfoMessages.anchor = GridBagConstraints.WEST;
		content.add(lblInfoMessages, gbcLblInfoMessages);
		
		lblMessage = new JLabel(" ");
		lblMessage.setName("lblMessage");
		GridBagConstraints gbcLabel = new MyGridBagConstraints(0, 8);
		content.add(lblMessage, gbcLabel);
		
		return panel;
	}
	
	DefaultListModel<Product> getListProductModel() {
		return listProductModel;
	}

	@Override
	public void listProducts(List<Product> products) {
		products.stream().forEach(listProductModel::addElement);
	}

	@Override
	public void showError(String message, Product product, String type) {
		lblMessage.setText(message + ": " + product.getName());
		if (type.contentEquals("error")) {
			lblMessage.setForeground(Color.RED);
		} else {
			lblMessage.setForeground(Color.BLACK);
		}
	}

	@Override
	public void productAdded(Product product) {
		listProductModel.addElement(product);
	}

	@Override
	public void productEdited(Product productBefore, Product productAfter) {
		listProductModel.set(listProductModel.indexOf(productBefore), productAfter);
	}

	@Override
	public void productDeleted(Product product) {
		listProductModel.removeElement(product);
	}
}
