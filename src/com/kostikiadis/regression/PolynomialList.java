package com.kostikiadis.regression;

import java.util.ArrayList;

public class PolynomialList extends ArrayList<Double> {

	public PolynomialList(final int p) {
		super(p);
	}

	public double getY(final double x) {
		double ret = 0;
		for (int p = 0; p < size(); p++) {
			ret += get(p) * (Math.pow(x, p));
		}
		return ret;
	}
}