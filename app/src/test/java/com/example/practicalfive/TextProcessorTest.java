package com.example.practicalfive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.util.Locale;

public class TextProcessorTest {
    private final TextProcessor processor = new TextProcessor();

    @Test
    public void acceptsAllFourMunicipalities() {
        assertEquals("Beijing", processor.processMunicipality("Beijing"));
        assertEquals("Shanghai", processor.processMunicipality("Shanghai"));
        assertEquals("Tianjin", processor.processMunicipality("Tianjin"));
        assertEquals("Chongqing", processor.processMunicipality("Chongqing"));
    }

    @Test
    public void normalisesCase() {
        assertEquals("Beijing", processor.processMunicipality("beijing"));
        assertEquals("Beijing", processor.processMunicipality("BEIJING"));
        assertEquals("Shanghai", processor.processMunicipality("SHANGHAI"));
        assertEquals("Tianjin", processor.processMunicipality("tianjin"));
        assertEquals("Chongqing", processor.processMunicipality("ChOnGqInG"));
    }

    @Test
    public void ignoresSurroundingWhitespace() {
        assertEquals("Beijing", processor.processMunicipality("  Beijing  "));
        assertEquals("Shanghai", processor.processMunicipality("\tshanghai\n"));
    }

    @Test
    public void rejectsMissingAndBlankInput() {
        assertNull(processor.processMunicipality(null));
        assertNull(processor.processMunicipality(""));
        assertNull(processor.processMunicipality("   "));
        assertNull(processor.processMunicipality("\t\r\n"));
        assertNull(processor.processMunicipality("\u2003"));
    }

    @Test
    public void rejectsUnsupportedOrPartialNames() {
        assertNull(processor.processMunicipality("Guangzhou"));
        assertNull(processor.processMunicipality("Beij"));
        assertNull(processor.processMunicipality("Beijing Shanghai"));
    }

    @Test
    public void worksIndependentlyOfTheDeviceLocale() {
        Locale previous = Locale.getDefault();
        try {
            Locale.setDefault(Locale.forLanguageTag("tr-TR"));
            assertEquals("Beijing", processor.processMunicipality("BEIJING"));
            assertEquals("Tianjin", processor.processMunicipality("TIANJIN"));
        } finally {
            Locale.setDefault(previous);
        }
    }
}
