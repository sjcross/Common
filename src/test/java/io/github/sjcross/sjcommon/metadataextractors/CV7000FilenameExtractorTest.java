package io.github.sjcross.sjcommon.metadataextractors;

import org.junit.jupiter.api.Test;

import io.github.sjcross.sjcommon.metadataextractors.CV7000FilenameExtractor;
import io.github.sjcross.sjcommon.metadataextractors.Metadata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CV7000FilenameExtractorTest {

    @Test
    public void testExtract() {
        String name = "AssayPlate_Greiner_#655090_C02_T0030F001L03A01Z02C10.tif";

        Metadata metadata = new Metadata();

        CV7000FilenameExtractor extractor = new CV7000FilenameExtractor();
        boolean success = extractor.extract(metadata,name);

        assertTrue(success);

        assertEquals("AssayPlate",metadata.getPlateName());
        assertEquals("Greiner",metadata.getPlateManufacturer());
        assertEquals("655090",metadata.getPlateModel());
        assertEquals("C02",metadata.getWell());
        assertEquals(30,metadata.getTimepoint());
        assertEquals(1,metadata.getField());
        assertEquals(3,metadata.getTimelineNumber());
        assertEquals(1,metadata.getActionNumber());
        assertEquals(2,metadata.getZ());
        assertEquals(10,metadata.getChannel());
        assertEquals("tif",metadata.getExt());

    }

    @Test
    public void testConstruct() {
        Metadata metadata = new Metadata();
        metadata.setPlateName("AssayPlate");
        metadata.setPlateManufacturer("Greiner");
        metadata.setPlateModel("655090");
        metadata.setWell("C02");
        metadata.setTimepoint(30);
        metadata.setField(1);
        metadata.setTimelineNumber(3);
        metadata.setActionNumber(1);
        metadata.setZ(2);
        metadata.setChannel(10);
        metadata.setExt("tif");

        String expected = "AssayPlate_Greiner_#655090_C02_T0030F001L03A01Z02C10.tif";

        CV7000FilenameExtractor extractor = new CV7000FilenameExtractor();
        String actual = extractor.construct(metadata);

        assertEquals(expected,actual);

    }
}