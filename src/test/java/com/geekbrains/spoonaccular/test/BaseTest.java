package com.geekbrains.spoonaccular.test;

import com.geekbrains.spoonaccular.AbstractTest;
import org.junit.jupiter.api.Test;

public class BaseTest extends AbstractTest {

    @Test
    void test() throws Exception {
        System.out.println(getResource("test.txt"));
    }

}
