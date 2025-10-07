package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UtilsTest {
    @Test
    void testSumAddsNumbers() {
        assertEquals(5, com.example.Utils.sum(2, 3));
    }
}
