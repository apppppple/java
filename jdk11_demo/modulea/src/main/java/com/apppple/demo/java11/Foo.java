package com.apppple.demo.java11;

/**
 * @author fanhui.mengfh on 2021/5/20
 */
public class Foo {
    private String a;
    private Integer b;
    private Float c;


    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public Integer getB() {
        return b;
    }

    public void setB(Integer b) {
        this.b = b;
    }

    public Float getC() {
        return c;
    }

    public void setC(Float c) {
        this.c = c;
    }


    class FooInner {
        private String d;

        public String getD() {
            return d;
        }

        public void setD(String d) {
            this.d = d;
        }
    }
}
