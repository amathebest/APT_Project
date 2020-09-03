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
import javax.swing.border.EmptyBorder;
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

import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JSplitPane;

public class ProductViewSwing extends JFrame implements ProductView {
	
	private static final long serialVersionUID = 1L;
	
	private transient ProductController productController;

	private JScrollPane scrollPane;
	private JList<Product> listProduct;
	private DefaultListModel<Product> listProductModel;
	
	private JTextField txtInsertProductName;
	private JTextField txtInsertProductQuantity;
	private JTextField txtEditProductProperties;

	private JButton removeProductButton;
	private JButton addProductButton;
	private JButton editProductButton;
	
	private JLabel lblMessage;
	
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
		scrollPane = new JScrollPane();
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
        GridBagLayout gbl_content = new GridBagLayout();
        gbl_content.columnWeights = new double[]{1.0};
        JPanel content = new JPanel(gbl_content);
        GridBagConstraints gbc_RightSide = new GridBagConstraints();
        panel.add(content);
        
        // Listeners
        
        KeyAdapter addProductButtonEnabler = new KeyAdapter() {
    		@Override
    		public void keyReleased(KeyEvent e) {
    			addProductButton.setEnabled(
    				!txtInsertProductName.getText().trim().isEmpty() && !txtInsertProductQuantity.getText().trim().isEmpty()
    			);
    		}
    	};
    	
    	KeyAdapter editProductButtonEnabler = new KeyAdapter() {
    		@Override
    		public void keyReleased(KeyEvent e) {
    			editProductButton.setEnabled(
    				!txtEditProductProperties.getText().trim().isEmpty() && listProduct.getSelectedIndex() != -1
    			);
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
        GridBagConstraints gbc_txtInsertProductName = new MyGridBagConstraints(0, 1);
        content.add(txtInsertProductName, gbc_txtInsertProductName);
        txtInsertProductName.setColumns(25);
        
        
        txtInsertProductQuantity = new JTextField();
        txtInsertProductQuantity.setName("quantityTextBox");
        txtInsertProductQuantity.addKeyListener(addProductButtonEnabler);
        GridBagConstraints gbc_txtInsertProductQuantity = new MyGridBagConstraints(0, 2);
        content.add(txtInsertProductQuantity, gbc_txtInsertProductQuantity);
        txtInsertProductQuantity.setColumns(25);
        
        
        JPanel buttonsSplit = new JPanel(new GridLayout(0, 2));
        GridBagConstraints gbc_buttonsSplit = new MyGridBagConstraints(0, 3);

		removeProductButton = new JButton("Remove");
		removeProductButton.setName("removeProductButton");
		removeProductButton.setEnabled(false);
		buttonsSplit.add(removeProductButton);

		addProductButton = new JButton("Add");
		addProductButton.setName("addProductButton");
		addProductButton.setEnabled(false);
		buttonsSplit.add(addProductButton);
		
		content.add(buttonsSplit, gbc_buttonsSplit);
		
		JLabel lblEditExistingProduct = new JLabel("Edit existing product");
		lblEditExistingProduct.setName("lblEditExistingProduct");
		GridBagConstraints gbclblEditExistingProduct = new MyGridBagConstraints(0, 4);
		gbclblEditExistingProduct.anchor = GridBagConstraints.WEST;
		content.add(lblEditExistingProduct, gbclblEditExistingProduct);
		
        txtEditProductProperties = new JTextField();
        txtEditProductProperties.setName("editPropertiesTextBox");
        txtEditProductProperties.addKeyListener(editProductButtonEnabler);
        GridBagConstraints gbc_txtEditProductProperties = new MyGridBagConstraints(0, 5);
        content.add(txtEditProductProperties, gbc_txtEditProductProperties);
        txtEditProductProperties.setColumns(25);
        
        
        JPanel editSplit = new JPanel(new GridLayout(0, 2));
        GridBagConstraints gbc_editSplit = new MyGridBagConstraints(0, 6);

        String nameString = "Name";
        JRadioButton nameButton = new JRadioButton(nameString);
        nameButton.setName("nameEditRadioButton");
        nameButton.setMnemonic(KeyEvent.VK_B);
        nameButton.setActionCommand(nameString);
        nameButton.setSelected(true);
        
        String quantityString = "Quantity";
        JRadioButton quantityButton = new JRadioButton(quantityString);
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
		editSplit.add(editProductButton);
		
		content.add(editSplit, gbc_editSplit);
		
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
		lblMessage.setText(message + ": " + product);
		if (type == "error") {
			lblMessage.setForeground(Color.RED);
		} else {
			lblMessage.setForeground(Color.BLACK);
		}
	}

	@Override
	public void productAdded(Product product) {
		listProductModel.addElement(product);
		lblMessage.setText(" ");
	}

	@Override
	public void productEdited(Product productBefore, Product productAfter) {
		listProductModel.setElementAt(productAfter, listProductModel.indexOf(productBefore));
		lblMessage.setText(" ");
	}

	@Override
	public void productDeleted(Product product) {
		listProductModel.removeElement(product);
		lblMessage.setText(" ");
	}
}
