package br.dev.dig.text.mask;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TextMaskDateTest {

    final TextMask date = new TextMask("____-__-__", '_', true);

    @Test
    void testDateFormatEmpty() {
        final String formatted = date.format("").toString();
        Assertions.assertEquals("____-__-__", formatted);
    }

    @Test
    void testDateFormat2() {
        final String formatted = date.format("2").toString();
        Assertions.assertEquals("2___-__-__", formatted);
    }

    @Test
    void testDateFormat2023() {
        final String formatted = date.format("2023").toString();
        Assertions.assertEquals("2023-__-__", formatted);
    }

    @Test
    void testDateFormatFull() {
        final String formatted = date.format("20230429").toString();
        Assertions.assertEquals("2023-04-29", formatted);
    }

    @Test
    void testDateFormatExtraTruncate() {
        final String formatted = date.format("202304295").toString();
        Assertions.assertEquals("2023-04-29", formatted);
    }

    @Test
    void testDateFormatAlreadyFormatted() {
        final String formatted = date.format("2023-04-29").toString();
        Assertions.assertEquals("2023-04-29", formatted);
    }
    @Test
    void testDateFormatBadlyFormatted() {
        final String formatted = date.format("2023/0429").toString();
        Assertions.assertEquals("2023-04-29", formatted);
    }

}
