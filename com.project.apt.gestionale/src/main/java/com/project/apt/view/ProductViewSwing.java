package com.project.apt.view;

import java.util.List;

import javax.swing.JFrame;

import com.project.apt.controller.ProductController;
import com.project.apt.model.Product;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import java.awt.Insets;
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

	private JList<Product> listProduct;
	private DefaultListModel<Product> listProductModel;
	
	private JTextField txtInsertProductName;
	private JTextField txtInsertProductQuantity;
	private JTextField txtEditProductProperties;

	private JButton removeProductButton;
	private JButton addProductButton;
	private JButton editProductButton;
	
	
	public void setProductController(ProductController productController) {
		this.productController = productController;
	}
	
	public ProductViewSwing() {
		setTitle("Product Manager");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
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
        JPanel content = new JPanel(new GridBagLayout());
        JPanel left = new JPanel(new GridLayout(2, 0));
        
        GridBagConstraints gbc_leftSide = new GridBagConstraints();
        gbc_leftSide.insets = new Insets(5, 0, 10, 10);
        gbc_leftSide.anchor = GridBagConstraints.EAST;
        
        

		JLabel lblProductList = new JLabel("Product list");
		lblProductList.setName("productListLabel");
		left.add(lblProductList);
		
		JScrollPane dataDisplay = new JScrollPane();
		listProductModel = new DefaultListModel<>();
		listProduct = new JList<>(listProductModel);
		listProduct.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listProduct.setName("productList");
		dataDisplay.setViewportView(listProduct);
        
        left.add(dataDisplay);
        
        content.add(left, gbc_leftSide);
        panel.add(content);
        
		return panel;
	}
	
	protected JPanel createRightPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        GridBagLayout gbl_content = new GridBagLayout();
        gbl_content.columnWeights = new double[]{1.0};
        JPanel content = new JPanel(gbl_content);
        GridBagConstraints gbc_RightSide = new GridBagConstraints();
        panel.add(content);
        
        
        txtInsertProductName = new JTextField();
        txtInsertProductName.setText("Insert product name here");
        txtInsertProductName.setName("nameTextBox");
        GridBagConstraints gbc_txtInsertProductName = createMyGBC(0, 0);
        content.add(txtInsertProductName, gbc_txtInsertProductName);
        txtInsertProductName.setColumns(10);
        
        
        txtInsertProductQuantity = new JTextField();
        txtInsertProductQuantity.setText("Insert product quantity here");
        txtInsertProductQuantity.setName("quantityTextBox");
        GridBagConstraints gbc_txtInsertProductQuantity = createMyGBC(0, 1);
        content.add(txtInsertProductQuantity, gbc_txtInsertProductQuantity);
        txtInsertProductQuantity.setColumns(10);
        
        
        JPanel buttonsSplit = new JPanel(new GridLayout(0, 2));
        GridBagConstraints gbc_buttonsSplit = createMyGBC(0, 2);

		removeProductButton = new JButton("Remove");
		removeProductButton.setName("removeProductButton");
		removeProductButton.setEnabled(false);
		buttonsSplit.add(removeProductButton);

		addProductButton = new JButton("Add");
		addProductButton.setName("addProductButton");
		addProductButton.setEnabled(false);
		buttonsSplit.add(addProductButton);
		
		content.add(buttonsSplit, gbc_buttonsSplit);
        
        
        txtEditProductProperties = new JTextField();
        txtEditProductProperties.setText("Insert new product details here");
        txtEditProductProperties.setName("editPropertiesTextBox");
        GridBagConstraints gbc_txtEditProductProperties = createMyGBC(0, 3);
        content.add(txtEditProductProperties, gbc_txtEditProductProperties);
        txtEditProductProperties.setColumns(10);
        
        
        JPanel editSplit = new JPanel(new GridLayout(0, 2));
        GridBagConstraints gbc_editSplit = createMyGBC(0, 4);

        String nameString = "Name";
        JRadioButton nameButton = new JRadioButton(nameString);
        nameButton.setMnemonic(KeyEvent.VK_B);
        nameButton.setActionCommand(nameString);
        nameButton.setSelected(true);
        
        String quantityString = "Quantity";
        JRadioButton quantityButton = new JRadioButton(quantityString);
        quantityButton.setMnemonic(KeyEvent.VK_B);
        quantityButton.setActionCommand(quantityString);
        
        ButtonGroup editChoice = new ButtonGroup();
        editChoice.add(nameButton);
        editChoice.add(quantityButton);
        
        
		editProductButton = new JButton("Edit");
		editProductButton.setName("editProductButton");
		editProductButton.setEnabled(false);
		editSplit.add(editProductButton);
		
		content.add(editSplit, gbc_editSplit);
		
		return panel;
	}
	
	protected GridBagConstraints createMyGBC(int x, int y) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 0, 10, 10);
		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridx = x;
		gbc.gridy = y;
		return gbc;
	}

	@Override
	public void listProducts(List<Product> products) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showError(String message, Product product, String type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void productAdded(Product product) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void productEdited(Product product) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void productDeleted(Product product) {
		// TODO Auto-generated method stub
		
	}
}
