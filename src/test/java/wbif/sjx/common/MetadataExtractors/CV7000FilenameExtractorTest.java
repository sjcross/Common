package wbif.sjx.common.MetadataExtractors;

import org.junit.Test;
import wbif.sjx.common.Object.HCMetadata;

import static org.junit.Assert.*;

public class CV7000FilenameExtractorTest {

    @Test
    public void testExtract() {
        String name = "AssayPlate_Greiner_#655090_C02_T0030F001L03A01Z02C10.tif";

        HCMetadata metadata = new HCMetadata();

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

    }
}