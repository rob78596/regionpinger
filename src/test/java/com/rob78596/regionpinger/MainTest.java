package com.rob78596.regionpinger;

import org.junit.Test;

public class MainTest {

    @Test
    public void test() {
        Main main = new Main();
        main.handleRequest(new Parameters("localhost", "localhost"), null);
    }
}
