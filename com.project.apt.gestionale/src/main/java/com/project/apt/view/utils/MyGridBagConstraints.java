package com.project.apt.view.utils;

import java.awt.GridBagConstraints;
import java.awt.Insets;

public class MyGridBagConstraints extends GridBagConstraints {
	
	private static final long serialVersionUID = 1L;

	public MyGridBagConstraints(int x, int y) {
		super();
		insets = new Insets(0, 0, 10, 10);
		gridx = x;
		gridy = y;
	}
}
