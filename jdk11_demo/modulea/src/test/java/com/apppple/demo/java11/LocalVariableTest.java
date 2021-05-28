package com.apppple.demo.java11;

import org.junit.Test;

import java.util.ArrayList;
import java.util.function.Function;

/**
 * <pre>
 *     局部变量类型测试
 * </pre>
 *
 * @author fanhui.mengfh on 2021/5/20
 */
public class LocalVariableTest {

    @Test
    public void test() {
        var foo = new Foo();
        // 推断为String
        var param = "a";
        foo.setA(param);
        var a = foo.getA();
        System.out.println(a);

        // 设别为Integer
        var intb = 1;
        foo.setB(1);
        // 无法给Float类型的c赋值
        // foo.setC(b);

        var doubleNumber = 20.5; // 推断为double
        var floatNumber = 20.5F; // 推断为float
        var productList = new ArrayList<>(); // 推断为ArrayList<Object>
        var productList2 = new ArrayList<String>(); // 推断为ArrayList<String>
        var numbers = new int[5];
        numbers[0] = 1;

        final var discount = 5;

        // 编译不通过
        // lambda表达式需要显式目标类型
        // var f = x -> x + 1;
        Function<Integer, Integer> f = x -> x + 1;

        // 编译通不过
        //var message = null; // 类型错误: 变量初始化为'null'



    }
    // 范型
    public <T extends Number> T add(T t) {
        var temp = t;
        return temp;
    }
}
