package com.example.lab1;

import org.junit.Test;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws IOException {

        assertEquals(4, 2 + 2);
    }

    @Test
    public void HTMLConvert() throws IOException {
        System.out.println(ParsingHtmlService.getHoliday("9 января 2020"));
    }


}