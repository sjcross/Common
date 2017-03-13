package wolfson.common.HighContent;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.text.DecimalFormat;

/**
 * Created by sc13967 on 27/10/2016.
 */
public class HCExporter {
    public File target_folder = null;
    public String root_name = "";
    public String output_ext = "";

    public String getRootName() {
        return root_name;
    }

    public void setRootName(String root_name) {
        this.root_name = root_name;
    }

    public File getTargetFolder() {
        return target_folder;

    }

    public void setTargetFolder(File target_folder) {
        this.target_folder = target_folder;

    }

    public void export(HCResultCollection results) {
        // If a root filename and target folder has been manually set, use this.  Otherwise, print an error stating no
        // name and/or folder was given
        if (!root_name.equals("") & target_folder != null) {
            export(results, target_folder, root_name);

        } else if (target_folder == null & !root_name.equals("")) {
            System.out.println("No target folder specified.  Results not saved");

        } else if (root_name.equals("") & target_folder != null) {
            System.out.println("No root filename specified.  Results not saved");

        } else {
            System.out.println("No root filename and target folder specified.  Results not saved");

        }
    }

    public void export(HCResultCollection results, String root_name) {
        // If a target folder has been manually set, use this.  Otherwise, print an error stating no target was given
        if (target_folder != null) {
            export(results, target_folder, root_name);

        } else {
            System.out.println("No target folder specified.  Results not saved");

        }
    }

    public void export(HCResultCollection results, File target_folder) {
        // If a root filename has been manually set, use this.  Otherwise, print an error stating no name was given
        if (!root_name.equals("")) {
            export(results, target_folder, root_name);

        } else {
            System.out.println("No root filename specified.  Results not saved");

        }
    }

    public void export(HCResultCollection results, File target_folder, String root_name){
        // Checking an overriding target folder hasn't already been specified
        if (this.target_folder != null) {
            target_folder = this.target_folder;

        }

        // Checking an overriding root filename hasn't already been specified
        if (!this.root_name.equals("")) {
            root_name = this.root_name;

        }

        runExportJob(results,target_folder, root_name);

    }

    public void runExportJob(HCResultCollection results, File target_folder, String root_name) {

    }

    public static Element summariseCollection(Document doc, HCResult res) {
        Element collection = doc.createElement("COLLECTION");

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


        Attr date = doc.createAttribute("DATE");
        if (res.getYear() != 0 & res.getMonth() != 0 & res.getDay() != 0) {
            date.appendChild(doc.createTextNode(String.valueOf(res.getDay())+"/"+String.valueOf(res.getMonth())+"/"+String.valueOf(res.getYear())));
        } else {
            date.appendChild(doc.createTextNode("NA"));
        }
        collection.setAttributeNode(date);


//        Attr year = doc.createAttribute("YEAR");
//        if (res.getYear() != 0) {
//            year.appendChild(doc.createTextNode(String.valueOf(res.getYear())));
//        } else {
//            year.appendChild(doc.createTextNode("NA"));
//        }
//        collection.setAttributeNode(year);
//
//
//        Attr month = doc.createAttribute("MONTH");
//        if (res.getMonth() != 0) {
//            month.appendChild(doc.createTextNode(String.valueOf(res.getMonth())));
//        } else {
//            month.appendChild(doc.createTextNode("NA"));
//        }
//        collection.setAttributeNode(month);
//
//
//        Attr day = doc.createAttribute("DAY");
//        if (res.getDay() != 0) {
//            day.appendChild(doc.createTextNode(String.valueOf(res.getDay())));
//        } else {
//            day.appendChild(doc.createTextNode("NA"));
//        }
//        collection.setAttributeNode(day);

        DecimalFormat time_df = new DecimalFormat("00");
        Attr time = doc.createAttribute("TIME");
        if (res.getHour() != 0 & res.getMin() != 0 & res.getSec() != 0) {
            time.appendChild(doc.createTextNode(String.valueOf(time_df.format(res.getHour()))+":"+String.valueOf(time_df.format(res.getMin()))+":"+String.valueOf(time_df.format(res.getSec()))));
        } else {
            time.appendChild(doc.createTextNode("NA"));
        }
        collection.setAttributeNode(time);


//        Attr hour = doc.createAttribute("HOUR");
//        if (res.getHour() != 0) {
//            hour.appendChild(doc.createTextNode(String.valueOf(res.getHour())));
//        } else {
//            hour.appendChild(doc.createTextNode("NA"));
//        }
//        collection.setAttributeNode(hour);
//
//
//        Attr min = doc.createAttribute("MINUTE");
//        if (res.getMin() != 0) {
//            min.appendChild(doc.createTextNode(String.valueOf(res.getMin())));
//        } else {
//            min.appendChild(doc.createTextNode("NA"));
//        }
//        collection.setAttributeNode(min);
//
//
//        Attr sec = doc.createAttribute("SECOND");
//        if (res.getSec() != 0) {
//            sec.appendChild(doc.createTextNode(String.valueOf(res.getSec())));
//        } else {
//            sec.appendChild(doc.createTextNode("NA"));
//        }
//        collection.setAttributeNode(sec);

        return collection;

    }

    public static Element summariseAll(Document doc, HCResult res) {
        Element collection = doc.createElement("COLLECTION");

        if (res.getFile1() != null) {
            Attr filepath = doc.createAttribute("FILEPATH");
            filepath.appendChild(doc.createTextNode(res.getFile1().getAbsolutePath()));
            collection.setAttributeNode(filepath);
        }

        if (res.getWell() != null) {
            Attr well = doc.createAttribute("WELL");
            well.appendChild(doc.createTextNode(String.valueOf(res.getWell())));
            collection.setAttributeNode(well);
        }

        if (res.getField() != -1) {
            Attr field = doc.createAttribute("FIELD");
            field.appendChild(doc.createTextNode(String.valueOf(res.getField())));
            collection.setAttributeNode(field);
        }

        if (res.getTimepoint() != -1) {
            Attr timepoint = doc.createAttribute("TIMEPOINT");
            timepoint.appendChild(doc.createTextNode(String.valueOf(res.getTimepoint())));
            collection.setAttributeNode(timepoint);
        }

        if (res.getZ() != -1) {
            Attr z_pos = doc.createAttribute("Z_POS");
            z_pos.appendChild(doc.createTextNode(String.valueOf(res.getZ())));
            collection.setAttributeNode(z_pos);
        }

        if (res.getChannel() != -1) {
            Attr channel = doc.createAttribute("CHANNEL");
            channel.appendChild(doc.createTextNode(String.valueOf(res.getChannel())));
            collection.setAttributeNode(channel);
        }

        if (res.getCelltype() != null) {
            Attr cell_type = doc.createAttribute("CELL_TYPE");
            cell_type.appendChild(doc.createTextNode(res.getCelltype()));
            collection.setAttributeNode(cell_type);
        }

        if (res.getMag() != null) {
            Attr magnification = doc.createAttribute("MAG");
            magnification.appendChild(doc.createTextNode(res.getMag()));
            collection.setAttributeNode(magnification);
        }

        if (res.getComment() != null) {
            Attr comment = doc.createAttribute("COMMENT");
            comment.appendChild(doc.createTextNode(res.getComment()));
            collection.setAttributeNode(comment);
        }

        if (res.getYear() != -1 & res.getMonth() != -1 & res.getDay() != -1) {
            Attr date = doc.createAttribute("DATE");
            date.appendChild(doc.createTextNode(String.valueOf(res.getDay())+"/"+String.valueOf(res.getMonth())+"/"+String.valueOf(res.getYear())));
            collection.setAttributeNode(date);
        }

        if (res.getHour() != -1 & res.getMin() != -1 & res.getSec() != -1) {
            DecimalFormat time_df = new DecimalFormat("00");
            Attr time = doc.createAttribute("TIME");

            time.appendChild(doc.createTextNode(String.valueOf(time_df.format(res.getHour()))+":"+String.valueOf(time_df.format(res.getMin()))+":"+String.valueOf(time_df.format(res.getSec()))));
            collection.setAttributeNode(time);
        } else if (res.getHour() != -1 & res.getMin() != -1 & res.getSec() == -1) {
            DecimalFormat time_df = new DecimalFormat("00");
            Attr time = doc.createAttribute("TIME");

            time.appendChild(doc.createTextNode(String.valueOf(time_df.format(res.getHour()))+":"+String.valueOf(time_df.format(res.getMin()))));
            collection.setAttributeNode(time);
        }

        return collection;

    }

    public static Element summariseExperiment(Document doc, HCResult res) {
        Element experiment = doc.createElement("EXPERIMENT");

        Attr filepath = doc.createAttribute("FILEPATH");
        if (res.getFile1() != null) {
            filepath.appendChild(doc.createTextNode(res.getFile1().getAbsolutePath()));
        } else {
            filepath.appendChild(doc.createTextNode("NA"));
        }
        experiment.setAttributeNode(filepath);

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

    public String getOutputExt() {
        return output_ext;
    }

    public void setOutputExt(String output_ext) {
        this.output_ext = output_ext;
    }
}