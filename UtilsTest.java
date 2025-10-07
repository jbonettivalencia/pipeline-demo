package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {
    @Test
    void testsumAddsNumbers() {
        assertEquals(5, com.example.Utils.sum(2, 3));
    }
}
