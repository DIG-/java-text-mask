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

    @Test
    void testDateIsCompleteEmpty() {
        Assertions.assertFalse(date.isComplete(""));
        Assertions.assertFalse(date.isComplete("____-__-__"));
    }

    @Test
    void testDateIsComplete2() {
        Assertions.assertFalse(date.isComplete("2___-__-__"));
    }

    @Test
    void testDateIsCompleteFull() {
        Assertions.assertTrue(date.isComplete("2023-04-29"));
    }

    @Test
    void testDateIsCompleteExtra() {
        Assertions.assertFalse(date.isComplete("2023-04-290"));
    }

    @Test
    void testDateIsCompleteInComplete() {
        Assertions.assertFalse(date.isComplete("2023-__-29"));
    }

    @Test
    void testDateUnFormatEmpty() {
        Assertions.assertEquals("", date.unformat("").toString());
        Assertions.assertEquals("", date.unformat("____-__-__").toString());
    }

    @Test
    void testDateUnFormat2() {
        Assertions.assertEquals("2", date.unformat("2___-__-__").toString());
    }

    @Test
    void testDateUnFormat2023() {
        Assertions.assertEquals("2023", date.unformat("2023-__-__").toString());
    }

    @Test
    void testDateUnFormatFull() {
        Assertions.assertEquals("20230429", date.unformat("2023-04-29").toString());
    }

}
