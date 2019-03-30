package com.huawei.ga;

import com.huawei.Main;
import com.huawei.map.Init;
import com.huawei.map.MapHelper;

/**
 * @Description:
 */

public class MainTest extends GeneticAlgorithm {
	public static int[] result = null;

	public MainTest(int geneSize, int geneNumber) {
		super(geneSize, geneNumber);
	}

	@Override
	public double[] changeX(Chromosome chro) {
		// decode
		int[] upper = { 1, 10000 };
		int[] lower = { 0, 1 };
		int gene_size = this.getGeneSize();
		int gene_number = this.getGeneNumber();

		int[] tmp = chro.getNum();
		int M = 1 << gene_size;
		double[] result = new double[gene_number];

		for (int i = 0; i < gene_number; i++) {
			// translate
			int dis = upper[i] - lower[i];
			result[i] = ((1.0 * tmp[i] / M) * dis) + lower[i];
		}
		return result;
	}

	@Override
	public double caculateY(double[] x) {
		// calculate fitness
		Init.getInstance().release();
		MapHelper.getInstance().release();
		Main.PassRate = x[0];
		Main.MaxCarsOnRoad = (int) x[1];
		Main.main(null);
		// schedule complete
		if (Init.getInstance().getTotalCars() == Init.getInstance().getScheduledCars()) {
			return Main.T;
		}
		return Integer.MAX_VALUE;
	}

	public static void main(String[] args) {
		MainTest test = new MainTest(12, 2);
		test.caculte();
	}
}