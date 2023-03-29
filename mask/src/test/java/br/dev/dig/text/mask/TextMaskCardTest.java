package br.dev.dig.text.mask;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TextMaskCardTest {

    final TextMask card = new TextMask("#### #### #### ####");

    @Test
    void testCardFormatEmpty() {
        final String formatted = card.format("").toString();
        Assertions.assertEquals("", formatted);
    }

    @Test
    void testCardFormat1() {
        final String formatted = card.format("1").toString();
        Assertions.assertEquals("1", formatted);
    }

    @Test
    void testCardFormat1234() {
        final String formatted = card.format("1234").toString();
        Assertions.assertEquals("1234", formatted);
    }

    @Test
    void testCardFormat1234_5() {
        final String formatted = card.format("12345").toString();
        Assertions.assertEquals("1234 5", formatted);
    }

    @Test
    void testCardFormatFull() {
        final String formatted = card.format("1234567812345678").toString();
        Assertions.assertEquals("1234 5678 1234 5678", formatted);
    }

    @Test
    void testCardFormatExtraTruncate() {
        final String formatted = card.format("12345678123456789").toString();
        Assertions.assertEquals("1234 5678 1234 5678", formatted);
    }

    @Test
    void testCardFormatAlreadyFormatted() {
        final String formatted = card.format("1234 5678 1234 5678").toString();
        Assertions.assertEquals("1234 5678 1234 5678", formatted);
    }

    @Test
    void testCardFormatBadlyFormatted() {
        final String formatted = card.format("1234-56781234 5678").toString();
        Assertions.assertEquals("1234 5678 1234 5678", formatted);
    }

    @Test
    void testCardIsCompleteEmpty() {
        Assertions.assertFalse(card.isComplete(""));
    }

    @Test
    void testCardIsComplete1() {
        Assertions.assertFalse(card.isComplete("1"));
    }

    @Test
    void testCardIsComplete1234() {
        Assertions.assertFalse(card.isComplete("1234"));
    }

    @Test
    void testCardIsComplete1234_5() {
        Assertions.assertFalse(card.isComplete("1234 5"));
    }

    @Test
    void testCardIsCompleteFull() {
        Assertions.assertTrue(card.isComplete("1234 5678 1234 5678"));
    }

    @Test
    void testCardUnFormatEmpty() {
        Assertions.assertEquals("", card.unformat("").toString());
    }

    @Test
    void testCardUnFormat1() {
        Assertions.assertEquals("1", card.unformat("1").toString());
    }

    @Test
    void testCardUnFormat1234() {
        Assertions.assertEquals("1234", card.unformat("1234").toString());
    }

    @Test
    void testCardUnFormat1234_5() {
        Assertions.assertEquals("12345", card.unformat("1234 5").toString());
    }

    @Test
    void testCardUnFormatFull() {
        Assertions.assertEquals("1234567812345678", card.unformat("1234 5678 1234 5678").toString());
    }

    @Test
    void testCardUnFormatExtra() {
        Assertions.assertEquals("12345678123456789", card.unformat("1234 5678 1234 56789").toString());
    }

    @Test
    void testCardUnFormatBadly() {
        Assertions.assertEquals("12345678234678", card.unformat("1234-56781234 5678").toString());
    }

}