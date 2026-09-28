package com.jamesstevens.rms.tests;

import com.jamesstevens.rms.Name;
import com.jamesstevens.rms.exceptions.IllegalParameterException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Name} validation and normalization.
 */
public class NameTest {

    /**
     * Verifies that standard names are normalized to title case.
     */
    @Test
    public void testStandardNameIsNormalized() {
        Name name = new Name("jAmEs", "sTeVeNs");

        assertEquals("James", name.getFirstName());
        assertEquals("Stevens", name.getLastName());
        assertEquals("James Stevens", name.toString());
    }

    /**
     * Verifies that surrounding whitespace is removed.
     */
    @Test
    public void testNameIsTrimmed() {
        Name name = new Name("  James  ", "  Stevens  ");

        assertEquals("James", name.getFirstName());
        assertEquals("Stevens", name.getLastName());
    }

    /**
     * Verifies that hyphenated names preserve the hyphen and normalize
     * each portion independently.
     */
    @Test
    public void testHyphenatedNameIsNormalized() {
        Name name = new Name("james", "sANTIAGO-mARTINEZ");

        assertEquals("James", name.getFirstName());
        assertEquals("Santiago-Martinez", name.getLastName());
    }

    /**
     * Verifies that names containing apostrophes preserve the apostrophe
     * and normalize each portion independently.
     */
    @Test
    public void testApostropheNameIsNormalized() {
        Name name = new Name("james", "o'bRIEN");

        assertEquals("James", name.getFirstName());
        assertEquals("O'Brien", name.getLastName());
    }

    /**
     * Verifies that a blank first name is rejected.
     */
    @Test
    public void testBlankFirstNameIsRejected() {
        assertThrows(
                IllegalParameterException.class,
                () -> new Name("   ", "Stevens")
        );
    }

    /**
     * Verifies that a blank last name is rejected.
     */
    @Test
    public void testBlankLastNameIsRejected() {
        assertThrows(
                IllegalParameterException.class,
                () -> new Name("James", "   ")
        );
    }

    /**
     * Verifies that a null first name is rejected.
     */
    @Test
    public void testNullFirstNameIsRejected() {
        assertThrows(
                IllegalParameterException.class,
                () -> new Name(null, "Stevens")
        );
    }

    /**
     * Verifies that a null last name is rejected.
     */
    @Test
    public void testNullLastNameIsRejected() {
        assertThrows(
                IllegalParameterException.class,
                () -> new Name("James", null)
        );
    }

    /**
     * Verifies that numeric characters are rejected.
     */
    @Test
    public void testNumericCharactersAreRejected() {
        assertThrows(
                IllegalParameterException.class,
                () -> new Name("James2", "Stevens")
        );
    }

    /**
     * Verifies that unsupported punctuation is rejected.
     */
    @Test
    public void testInvalidPunctuationIsRejected() {
        assertThrows(
                IllegalParameterException.class,
                () -> new Name("James", "Stevens!")
        );
    }
}
