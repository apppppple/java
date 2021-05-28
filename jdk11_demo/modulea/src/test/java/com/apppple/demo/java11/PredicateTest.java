package com.apppple.demo.java11;

import org.junit.Test;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

/**
 * @author fanhui.mengfh on 2021/5/20
 */
public class PredicateTest {
    @Test
    public void test_Predicate_Not() {

        String[] names = {"TEST", "MARY", " ", ""};

        List loweCaseList = Stream.of(names)
            .filter(Predicate.not(String::isBlank))
            .collect(Collectors.toList());
        assertEquals(2, loweCaseList.size());
    }
}
