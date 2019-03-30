package com.huawei.ga;

/**  
*@Description: 基因遗传染色体   
*/

import java.util.ArrayList;
import java.util.List;

public class Chromosome {
	private boolean[] gene;// 基因序列
	private double score;// 对应的函数得分
	private int geneSize;// 基因最大长度
	private int geneNumber;// 基因个数

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public int getGeneSize() {
		return geneSize;
	}

	public void setGeneSize(int geneSize) {
		this.geneSize = geneSize;
	}

	public int getGeneNumber() {
		return geneNumber;
	}

	public void setGeneNumber(int geneNumber) {
		this.geneNumber = geneNumber;
	}

	/**
	 * 随机生成基因序列
	 * 
	 * @param size
	 * @param number
	 */
	public Chromosome(int size, int number) {
		if (size <= 0) {
			return;
		}
		this.geneNumber = number;
		this.geneSize = size;
		initGeneSize(size, number);
		for (int i = 0; i < size; i++) {
			gene[i] = Math.random() >= 0.5;
		}
	}

	/**
	 * @param c
	 * @return
	 * @Author:lulei
	 * @Description: 克隆基因
	 */
	public static Chromosome clone(final Chromosome c) {
		if (c == null || c.gene == null) {
			return null;
		}
		Chromosome copy = new Chromosome(c.geneSize, c.geneNumber);
		copy.initGeneSize(c.geneSize, c.geneNumber);
		for (int i = 0; i < c.gene.length; i++) {
			copy.gene[i] = c.gene[i];
		}
		return copy;
	}

	/**
	 * @param size
	 * @Author:lulei
	 * @Description: 初始化基因长度
	 */
	private void initGeneSize(int size, int number) {
		if (size <= 0) {
			return;
		}
		gene = new boolean[size * number];
	}

	/**
	 * @param c1
	 * @param c2
	 * @Author:lulei
	 * @Description: 遗传产生下一代
	 */
	public static List<Chromosome> genetic(Chromosome p1, Chromosome p2) {
		if (p1 == null || p2 == null) { // 染色体有一个为空，不产生下一代
			return null;
		}
		if (p1.gene == null || p2.gene == null) { // 染色体有一个没有基因序列，不产生下一代
			return null;
		}
		if (p1.gene.length != p2.gene.length) { // 染色体基因序列长度不同，不产生下一代
			return null;
		}
		Chromosome c1 = clone(p1);
		Chromosome c2 = clone(p2);
		// 随机产生交叉互换位置
		int size = c1.gene.length;
		int a = ((int) (Math.random() * size)) % size;
		int b = ((int) (Math.random() * size)) % size;
		int min = a > b ? b : a;
		int max = a > b ? a : b;
		// 对位置上的基因进行交叉互换
		for (int i = min; i <= max; i++) {
			boolean t = c1.gene[i];
			c1.gene[i] = c2.gene[i];
			c2.gene[i] = t;
		}
		List<Chromosome> list = new ArrayList<Chromosome>();
		list.add(c1);
		list.add(c2);
		return list;
	}

	/**
	 * @param num
	 * @Author:lulei
	 * @Description: 基因num个位置发生变异
	 */
	public void mutation(int num) {
		// 允许变异
		int size = gene.length;
		for (int i = 0; i < num; i++) {
			// 寻找变异位置
			int at = ((int) (Math.random() * size)) % size;
			// 变异后的值
			boolean bool = !gene[at];
			gene[at] = bool;
		}
	}

	/**
	 * @return
	 * @Author:lulei
	 * @Description: 将基因转化为对应的数字
	 */
	public int[] getNum() {
		if (gene == null) {
			return null;
		}
		int[] num = new int[geneNumber];
		for (int i = 0; i < geneNumber; i++) {
			for (int j = i * geneNumber; j < geneSize; j++) {
				// decode
				num[i] <<= 1;
				// bit j is 1
				if (gene[j]) {
					num[i] += 1;
				}
			}
		}
		return num;
	}
}