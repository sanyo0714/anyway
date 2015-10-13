package com.sanyo.arithmetic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Box {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		System.out.print("请输入球的数量：");
		BufferedReader boll = new BufferedReader(new InputStreamReader(
				System.in));
		String bollNumStr = null;
		bollNumStr = boll.readLine();

		System.out.print("请输入箱子的数量：");
		BufferedReader box = new BufferedReader(
				new InputStreamReader(System.in));
		String boxNumStr = null;
		boxNumStr = box.readLine();
		System.out.println("\n");

		int bollNum = -1;
		int boxNum = -1;

		try {
			bollNum = Integer.valueOf(bollNumStr);
		} catch (NumberFormatException e) {
			System.out.println("输入球的数量错误，数量不是整数");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			boxNum = Integer.valueOf(boxNumStr);
		} catch (NumberFormatException e) {
			System.out.println("输入箱子的数量错误，数量不是整数");
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (bollNum != -1 && boxNum != -1) {
			System.out.println("球的数量是：" + bollNum);
			System.out.println("箱子的数量是：" + boxNum);
			ArrayList<String> list = new ArrayList<String>();

			if (boxNum < 2) {
				System.out.println(bollNum);
			} else {
				list = twoBox(bollNum, 0, list);

				for (int i = 0; boxNum - 2 > i; i++) {
					list = allBox(list);
				}

				for (String ss : list) {
					System.out.println(ss);
				}
			}

		}
	}

	/**
	 * 将传入的list的每一个元素的第一个数拆分成两个数，并重组数组
	 * @param list
	 * @return 重组后的数组
	 */
	public static ArrayList<String> allBox(ArrayList<String> list) {
		ArrayList<String> listback = new ArrayList<String>();
		for (String s : list) {
			StringBuffer sbs = new StringBuffer(s);
			String[] ss = s.split(" ");
			int i = Integer.valueOf(ss[1]);
			ArrayList<String> partlist = new ArrayList<String>();
			partlist = twoBox(Integer.valueOf(ss[0]) - i, i, partlist);
			for (String str : partlist) {
				if (Integer.valueOf(str.split(" ")[0]) >= i)
					listback.add(str + " " + sbs.substring(ss[0].length() + 1));
			}
		}
		return listback;
	}

	/**
	 * 将一个数分成两数，并组成一个数据（例如：5   分成[5 0,4 1,3 2]）
	 * @param first
	 * @param second
	 * @param list
	 * @return 组合好的list
	 */
	public static ArrayList<String> twoBox(int first, int second,
			ArrayList<String> list) {
		list.add(first + " " + second);
		if (first > second + 1) {
			list = twoBox(first - 1, second + 1, list);
		}
		return list;
	}

}
