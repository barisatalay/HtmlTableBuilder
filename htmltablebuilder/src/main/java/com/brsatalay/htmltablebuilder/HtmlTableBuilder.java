package com.brsatalay.htmltablebuilder;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.brsatalay.htmltablebuilder.constant.HtmlConstant;
import com.brsatalay.htmltablebuilder.model.HtmlTableBuilderListener;
import com.brsatalay.htmltablebuilder.model.StringWrapper;
import com.brsatalay.htmltablebuilder.model.enumeration.enmAlignment;
import com.brsatalay.htmltablebuilder.model.enumeration.enmCellValueType;
import com.brsatalay.htmltablebuilder.model.mdlFooterCell;
import com.brsatalay.htmltablebuilder.model.mdlGridCell;
import com.brsatalay.htmltablebuilder.model.mdlHeaderCell;
import com.brsatalay.htmltablebuilder.utils.UtilsFile;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class HtmlTableBuilder {
    private final String TAG = "HtmlTableBuilder";
    private StringBuffer textTemplate;
    private HtmlTableBuilderListener listener;
    private List<String> cssList;
    private Context mContext;

    private boolean isFooterAvailable;
    /**
     * This text display if cells string bigger then 1000000 chacter
     * */
    private String outOfMemoryText;
    /**
     * This text display if not found any data
     * */
    private String noDataText;
    /**
     * field not display if not contain in headerInfos
     * */
    private LinkedHashMap<String, mdlHeaderCell> headerInfos;
    /**
     * The data to be displayed on the screen is included in this.
     * */
    private List<LinkedHashMap<String,mdlGridCell>> rowList;
    /**
     * field not display on footer if not contain in footerInfos.
     * */
    private LinkedHashMap<String, mdlFooterCell> footerInfos;
    private List dataSet;

    public HtmlTableBuilder(Context mContext, boolean isFooterAvailable) {
        this.mContext = mContext;
        this.isFooterAvailable = isFooterAvailable;
        clearAll();
    }

    public String build(String fileName){
        catchFieldDataTypes();

        prepareHeader();
        prepareCells();
        prepareFooter();

        writeTemplateToStyleBegin();
        writeCSS();
        writeTemplateToStyleEnd();

        writeTemplateToHeaderBegin();
        writeHeader();
        writeTemplateToHeaderEnd();

        writeTemplateToBodyBegin();
        writeCells();
        writeTemplateToBodyEnd();

        if (isFooterAvailable) {
            writeTemplateToFooterBegin();
            writeFooter();
            writeTemplateToFooterEnd();
        }

        try {
            if (textTemplate.length() < 1000000){
                return UtilsFile.stringToFile(mContext, fileName, textTemplate.toString());
            }else{
                textTemplate.setLength(0);
                writeTemplateToStyleBegin();
                writeCSS();
                writeTemplateToStyleEnd();

                writeTemplateToHeaderBegin();
                writeHeader();
                writeTemplateToHeaderEnd();

                writeTemplateToBodyBegin();
                writeValue("<span>");
                writeValue(outOfMemoryText);
                writeValue("</span>");
                writeTemplateToBodyEnd();

                return UtilsFile.stringToFile(mContext, fileName, textTemplate.toString());
            }
        }finally {
            clearAll();
        }
    }

    private void clearAll(){
        if (dataSet != null)
            dataSet.clear();
        rowList = new ArrayList<>();
        cssList = new ArrayList<>();
        clearFooterValues();
        if (textTemplate == null)
            textTemplate = new StringBuffer("");
        else
            textTemplate.setLength(0);
    }

    private void clearFooterValues() {
        for (String fieldName : getFooterInfos().keySet())
            getFooterInfos().get(fieldName).setValue("");
    }

    public LinkedHashMap<String, mdlFooterCell> getFooterInfos() {
        if (footerInfos == null)
            footerInfos = new LinkedHashMap<>();
        return footerInfos;
    }

    public void setFooterInfos(LinkedHashMap<String, mdlFooterCell> footerInfos) {
        this.footerInfos = footerInfos;
    }

    public void setListener(HtmlTableBuilderListener listener) {
        this.listener = listener;
    }

    private void prepareHeader() {
        StringBuilder cssTemplate = new StringBuilder();
        cssTemplate.append(".header ").append("\n")
                .append("{").append("\n")
                .append("font-weight: bold; ").append("\n")
                .append("background-image: linear-gradient(bottom, rgb(230,230,230) 0%, rgb(255,255,255) 68%);").append("\n")
                .append("background-image: -o-linear-gradient(bottom, rgb(230,230,230) 0%, rgb(255,255,255) 68%);").append("\n")
                .append("background-image: -moz-linear-gradient(bottom, rgb(230,230,230) 0%, rgb(255,255,255) 68%);").append("\n")
                .append("background-image: -webkit-linear-gradient(bottom, rgb(230,230,230) 0%, rgb(255,255,255) 68%);").append("\n")
                .append("background-image: -ms-linear-gradient(bottom, rgb(230,230,230) 0%, rgb(255,255,255) 68%);").append("\n")
                .append("").append("\n")
                .append("background-image: -webkit-gradient(").append("\n")
                .append("linear,").append("\n")
                .append("left bottom,").append("\n")
                .append("left top,").append("\n")
                .append("color-stop(0, rgb(230,230,230)),").append("\n")
                .append("color-stop(0.68, rgb(255,255,255))").append("\n")
                .append(");").append("\n")
                .append("height:")
                .append(HtmlConstant.HeaderHeight)
                .append("px;")
                .append("\n")
                .append("font-size:")
                .append(HtmlConstant.FontSize)
                .append("px;")
                .append("\n")
                .append("font-family: Tahoma, Geneva, sans-serif;").append("\n")
                .append("}").append("\n");

        cssTemplate.append(".footer").append("\n")
                .append("{").append("\n")
                .append("background-color: rgb(202,81,0); ").append("\n")
                .append("height:").append(HtmlConstant.FooterHeight)
                .append("px;")
                .append("\n")
                .append("font-size:").append(HtmlConstant.FontSize - 4)
                .append("px;")
                .append("\n")
                .append("color: white; ").append("\n")
                .append("font-family: Tahoma, Geneva, sans-serif; ").append("\n")
                .append("}").append("\n");

        cssList.add(cssTemplate.toString());
    }

    private void writeTableEnd() {
        writeValue("</table>");
        writeValue("\n");
    }

    private void writeTDEnd() {
        writeValue("</TD>");
        writeValue("\n");
    }

    private void writeValue(String value) {
        textTemplate.append(value);
    }

    public void setDataSet(List dataSet) {
        this.dataSet = dataSet;
    }

    private void prepareCells() {
        for (int index = 0; index < dataSet.size(); index++) {
            Object row = dataSet.get(index);
            Class cls = row.getClass();
            Field fields[] = cls.getDeclaredFields();
            HashMap<String, String> fieldList = prepareFieldList(fields, row, cls);

            LinkedHashMap<String,mdlGridCell> column = new LinkedHashMap<>();
            mdlGridCell gridCell = null;

            for (String fieldName : getHeaderInfos().keySet()){
                gridCell = new mdlGridCell();

                mdlHeaderCell headerCell = getHeaderInfos().get(fieldName);

                StringWrapper fieldValue = new StringWrapper(fieldList.get(fieldName));

                if (headerCell.getAlignment() == enmAlignment.RightJustify){
                    fieldValue.add(" ");
                }

                gridCell.setFontSize(headerCell.getFontSize());
                gridCell.setFieldName(fieldName);
                gridCell.setWidth(headerCell.getWidth());
                gridCell.setAlignment(headerCell.getAlignment());
                gridCell.setDisplayFormat(headerCell.getDisplayFormat());
                gridCell.setValueType(headerCell.getValueType());

                gridCell.setValue(fieldValue.toString());

                column.put(fieldName, gridCell);
            }

            if (gridCell != null)
                rowList.add(column);
        }
    }

    public LinkedHashMap<String, mdlHeaderCell> getHeaderInfos() {
        if (headerInfos == null)
            headerInfos = new LinkedHashMap<>();
        return headerInfos;
    }

    public void setHeaderInfos(LinkedHashMap<String, mdlHeaderCell> headerInfos) {
        this.headerInfos = headerInfos;
    }

    /**
     * Preparing field to be shown on the screen
     * */
    private HashMap<String, String> prepareFieldList(Field[] fields, Object row, Class cls) {
        HashMap<String, String> hashMap = new HashMap<>();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (!field.isAccessible())
                field.setAccessible(true);
            Object object = null;
            String fieldName = "";
            String fieldValue = "";
            try {
                object = field.get(row);
            } catch (IllegalAccessException e) {
                Log.e(TAG,"The field " + field.getName() + " of " + cls.getSimpleName() + " is not accessible!");
            }
            fieldName = field.getName();
            if (object == null) {
                Log.e(TAG,"The field " + field.getName() + " of " + cls.getSimpleName() + " is null!");
                fieldValue = "";

                if (object instanceof Double) {
                    fieldValue = "0.0";
                } else if (object instanceof Integer) {
                    fieldValue = "0";
                }
            } else {
                fieldValue = String.valueOf(object);
            }
            /**
             * field not display if not contain in headerInfos
             * */
            if (TextUtils.isEmpty(fieldName) || !getHeaderInfos().containsKey(fieldName))
                continue;

            hashMap.put(fieldName, fieldValue);
        }

        return hashMap;
    }

    private void prepareFooter() {
        if (!isFooterAvailable)
            return;

        for (int i=0;i<rowList.size();i++){
            LinkedHashMap<String, mdlGridCell> cellList = rowList.get(i);

            for(String fieldName: cellList.keySet())
                footerCalc(fieldName, cellList.get(fieldName));
        }
    }

    private void footerCalc(String fieldName, mdlGridCell cell) {
        if (!getFooterInfos().containsKey(fieldName))
            //For those who are not in the footer list, the process will not continue.
            return;
        mdlFooterCell footerCell = getFooterInfos().get(fieldName);

        switch (footerCell.getType()){
            case Count:
                footerCell.setValue(String.valueOf(footerCell.toInteger() + 1));
                break;
            case Sum:
                if (cell.isValueDoubleOrDecimal()) {
                    double va = cell.toDouble();

                    footerCell.setValue(String.valueOf(footerCell.toDouble() + va));
                }else{
                    int va = cell.toInteger();

                    footerCell.setValue(String.valueOf(footerCell.toInteger() + va));
                }
        }
    }

    private void writeTemplateToStyleBegin() {
        writeValue("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n" +
                "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
                "<HTML xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\n" +
                "<BODY style=\"margin:0px\">\n" +
                "\n" +
                "<style>");
    }

    private void writeTemplateToFooterEnd() {
        writeValue("</div>\n");
    }

    private void writeTemplateToFooterBegin() {
        writeValue("<div id=\"div_tbl_footer\" style=\"width:100%;\">\n");
    }

    private void writeTemplateToBodyEnd() {
        writeValue("</div>");
    }

    private void writeTemplateToBodyBegin() {
        writeValue("<div id=\"div_tbl_main\" style=\"width:100%\">\n");
    }

    private void writeTemplateToHeaderEnd() {
        writeValue("</div>");
        writeValue("\n\n");
    }

    private void writeTemplateToHeaderBegin() {
        writeValue("<div id=\"div_tbl_header\" style=\"width:100%;\">");
    }

    private void writeTemplateToStyleEnd() {
        writeValue("</style>");
        writeValue("\n\n");
    }

    private void writeCSS() {
        StringBuilder result = new StringBuilder();
        if (cssList.size() >= 1)
            result.append(cssList.get(0)).append("\n");

        for (int i = 0; i < cssList.size(); i++) {
            result.append(cssList.get(i));
        }

        writeValue(result.toString());
    }

    private void writeHeader() {
        writeTable("tbl_header");
        int index = 0;
        for (String fieldName : getHeaderInfos().keySet()){
            mdlHeaderCell headerCell = getHeaderInfos().get(fieldName);

            if (listener != null)
                listener.onDrawColumnHeader(index, headerCell);

            writeTD(headerCell, "", "header");

            writeValue(headerCell.toString() + "  ");

            writeTDEnd();

            index++;
        }
        writeTableEnd();
    }

    private void writeTable(String id){
        writeValue("<table " + (!TextUtils.isEmpty(id)?"id='" + id + "'": "") + "border='1' cellspacing='1' cellpadding='3' rules='cols' frame='vsides' style='table-layout:fixed' width='");

        int width = 0;
        for (String fieldName : getHeaderInfos().keySet())
            width = width + getHeaderInfos().get(fieldName).getWidth();

        writeValue(width + "px");

        writeValue("'>\n");
    }

    private void writeTR() {
        textTemplate.append("<tr>").append("\n");
    }

    private void writeTD(mdlGridCell cell,String align, String cssClassName){
        String alignString = (!TextUtils.isEmpty(align)?"align='" + align + "'": "");

        if (TextUtils.isEmpty(cssClassName) || (cellHaveAnyStyle(cell)))
            writeValue("<TD Width='" + cell.getWidth() + "'  style='" + cssGenerator(cell) + "' "+ alignString + ">");
        else
            writeValue("<TD Width='" + cell.getWidth() + "'  class='" + cssClassName + "' "+ alignString + ">");
    }

    private boolean cellHaveAnyStyle(mdlGridCell cell) {
        if (cell == null)
            return false;
        return !TextUtils.isEmpty(cell.getBackgroundColor()) || !TextUtils.isEmpty(cell.getTextColor()) ;
    }

    private String cssGenerator(mdlGridCell cell){
        StringBuilder cssVal = new StringBuilder();
        cssVal.append("background-color: ").append(cell.getBackgroundColor()).append(";");
        cssVal.append("color:").append(cell.getTextColor()).append(";")
                .append("font-family:").append("Tahoma, Geneva, sans-serif").append(";")
                .append("font-size:").append(cell.getFontSize()).append("px;");
        return cssVal.toString();
    }

    private void writeCells() {
        writeTable("tbl_main");
        writeNoDataText();
        for(int rowIndex = 0; rowIndex< rowList.size(); rowIndex++){
            LinkedHashMap<String,mdlGridCell> row = rowList.get(rowIndex);
            writeTR();
            if (listener != null){
                listener.onCalcFields(rowIndex, row);
            }
            for(String fieldName: row.keySet()){
                mdlGridCell cell = row.get(fieldName);

                if (listener != null)
                    listener.onDrawCell(rowIndex, cell);

                String align = convertAlignmentToStr(cell.getAlignment());

                writeTD(cell, align, "");

                writeValue(cell.toString());
                writeTDEnd();
            }
        }
    }

    private void writeNoDataText() {
        if (rowList.size() == 0){
            if (noDataText != null)
                writeValue(noDataText);
            return;
        }
    }

    private String convertAlignmentToStr(enmAlignment alignment) {
        switch (alignment){
            case Center: return "center";
            case LeftJustify: return "left";
            case RightJustify: return "right";
            default: return "";
        }
    }

    private void writeFooter() {
        writeTable("tbl_footer");

        for (String fieldName : getHeaderInfos().keySet()){
            mdlHeaderCell headerCell = getHeaderInfos().get(fieldName);

            if (!getFooterInfos().containsKey(fieldName)) {
                mdlFooterCell footerCell = new mdlFooterCell.Builder()
                        .width(headerCell.getWidth())
                        .value("")
                        .build();


                writeTD(footerCell, "", "footer");

                writeValue(footerCell.toString() + "  ");

                writeTDEnd();
                continue;
            }

            mdlFooterCell footerCell = getFooterInfos().get(fieldName);

            footerCell.setWidth(headerCell.getWidth());

            String align = convertAlignmentToStr(headerCell.getAlignment());

            writeTD(footerCell, align, "footer");

            writeValue(footerCell.toString() + "  ");

            writeTDEnd();
        }
        writeTableEnd();
    }

    private void catchFieldDataTypes() {
        for (String fieldName : getHeaderInfos().keySet()){
            mdlGridCell headerCell = getHeaderInfos().get(fieldName);

            if (getFooterInfos().containsKey(fieldName)) {
                mdlFooterCell footerCell = getFooterInfos().get(fieldName);
                if (footerCell.getValueType() == enmCellValueType.Unknow)
                    footerCell.setValueType(headerCell.getValueType());

                if (TextUtils.isEmpty(footerCell.getDisplayFormat()))
                footerCell.setDisplayFormat(headerCell.getDisplayFormat());
            }
        }
    }

    public void setNoDataText(String noDataText) {
        this.noDataText = noDataText;
    }

    public void setOutOfMemoryText(String outOfMemoryText) {
        this.outOfMemoryText = outOfMemoryText;
    }
}
