package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UtilsTest {

    @Test
    public void testSumAddsNumbers() {
        assertEquals(5, Utils.sum(2, 3));
    }
}
