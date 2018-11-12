package com.example.demo.test;

public class Calculator {

    /**
     * 传入两个参数，求和
     *
     * @param a
     * @param b
     * @return
     */
    public int add(int a, int b) {
        return a + b;
    }

    /**
     * 传入两个参数，求差
     *
     * @param a
     * @param b
     * @return
     */
    public int sub(int a, int b) {
        return a - b;
    }

    public static void main(String[] args) {
        Calculator c = new Calculator();
        //测试 add()方法
        int result = c.add(1, 2);
        if(result == 3){
            System.out.println("add()方法正确");
        }

        //测试 sub()方法
        int result2 = c.sub(2, 1);
        if(result2 == 1){
            System.out.println("sub()方法正确");
        }

    }
}
