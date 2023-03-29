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

}