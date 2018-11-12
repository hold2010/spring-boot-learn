package com.example.demo.test;

import org.junit.*;

public class CalculatorTest {

    public CalculatorTest() {
        System.out.println("构造函数");
    }

    @BeforeClass
    public static void beforeClass(){
        System.out.println("@BeforeClass");
    }

    @Before
    public void before(){
        System.out.println("@Before");
    }

    @Ignore
    public void ignore(){
        System.out.println("@Ignore");
    }

    @After
    public void after(){
        System.out.println("@After");
    }

    @AfterClass
    public static void afterClass(){
        System.out.println("@AfterClass");
    }

    @Test
    public void add() {
        Calculator c = new Calculator();
        int result = c.add(1, 2);
        Assert.assertEquals(result, 3);
    }

    @Test
    public void sub() {
        Calculator c = new Calculator();
        int result = c.sub(2, 1);
        Assert.assertEquals(result, 1);
    }
}