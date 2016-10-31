package wolfson.common.HighContent;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;

/**
 * Created by sc13967 on 27/10/2016.
 */
public abstract class HCExporter {
    public void export(HCResultCollection HC_result_collection) {

    }

    public void export(HCResultCollection HC_result_collection, File file){

    }

    public static Element summariseCollection(Document doc, HCResult res) {
        Element collection = doc.createElement("COLLECTION");

        Attr filepath = doc.createAttribute("FILEPATH");
        if (res.getFile1() != null) {
            filepath.appendChild(doc.createTextNode(res.getFile1().getAbsolutePath()));
        } else {
            filepath.appendChild(doc.createTextNode("NA"));
        }
        collection.setAttributeNode(filepath);


        Attr cell_type = doc.createAttribute("CELL_TYPE");
        if (res.getCelltype() != null) {
            cell_type.appendChild(doc.createTextNode(res.getCelltype()));
        } else {
            cell_type.appendChild(doc.createTextNode("NA"));
        }
        collection.setAttributeNode(cell_type);


        Attr magnification = doc.createAttribute("MAG");
        if (res.getMag() != null) {
            magnification.appendChild(doc.createTextNode(res.getMag()));
        } else {
            magnification.appendChild(doc.createTextNode("NA"));
        }
        collection.setAttributeNode(magnification);


        Attr comment = doc.createAttribute("COMMENT");
        if (res.getComment() != null) {
            comment.appendChild(doc.createTextNode(res.getComment()));
        } else {
            comment.appendChild(doc.createTextNode("NA"));
        }
        collection.setAttributeNode(comment);


        Attr year = doc.createAttribute("YEAR");
        if (res.getYear() != 0) {
            year.appendChild(doc.createTextNode(String.valueOf(res.getYear())));
        } else {
            year.appendChild(doc.createTextNode("NA"));
        }
        collection.setAttributeNode(year);


        Attr month = doc.createAttribute("MONTH");
        if (res.getMonth() != 0) {
            month.appendChild(doc.createTextNode(String.valueOf(res.getMonth())));
        } else {
            month.appendChild(doc.createTextNode("NA"));
        }
        collection.setAttributeNode(month);


        Attr day = doc.createAttribute("DAY");
        if (res.getDay() != 0) {
            day.appendChild(doc.createTextNode(String.valueOf(res.getDay())));
        } else {
            day.appendChild(doc.createTextNode("NA"));
        }
        collection.setAttributeNode(day);


        Attr hour = doc.createAttribute("HOUR");
        if (res.getHour() != 0) {
            hour.appendChild(doc.createTextNode(String.valueOf(res.getHour())));
        } else {
            hour.appendChild(doc.createTextNode("NA"));
        }
        collection.setAttributeNode(hour);


        Attr min = doc.createAttribute("MINUTE");
        if (res.getMin() != 0) {
            min.appendChild(doc.createTextNode(String.valueOf(res.getMin())));
        } else {
            min.appendChild(doc.createTextNode("NA"));
        }
        collection.setAttributeNode(min);


        Attr sec = doc.createAttribute("SECOND");
        if (res.getSec() != 0) {
            sec.appendChild(doc.createTextNode(String.valueOf(res.getSec())));
        } else {
            sec.appendChild(doc.createTextNode("NA"));
        }
        collection.setAttributeNode(sec);

        return collection;

    }

    public static Element summariseExperiment(Document doc, HCResult res) {
        Element experiment = doc.createElement("EXPERIMENT");

        Attr well = doc.createAttribute("WELL");
        well.appendChild(doc.createTextNode(String.valueOf(res.getWell())));
        experiment.setAttributeNode(well);

        Attr field = doc.createAttribute("FIELD");
        field.appendChild(doc.createTextNode(String.valueOf(res.getField())));
        experiment.setAttributeNode(field);

        Attr timepoint = doc.createAttribute("TIMEPOINT");
        timepoint.appendChild(doc.createTextNode(String.valueOf(res.getTimepoint())));
        experiment.setAttributeNode(timepoint);

        Attr z_pos = doc.createAttribute("Z_POS");
        z_pos.appendChild(doc.createTextNode(String.valueOf(res.getZ())));
        experiment.setAttributeNode(z_pos);

        Attr channel = doc.createAttribute("CHANNEL");
        channel.appendChild(doc.createTextNode(String.valueOf(res.getChannel())));
        experiment.setAttributeNode(channel);

        return experiment;

    }

}