package com.brsatalay.htmltablebuilder.model;

import android.text.TextUtils;
import android.util.Log;

import com.brsatalay.htmltablebuilder.constant.FormatSettings;
import com.brsatalay.htmltablebuilder.constant.HtmlConstant;
import com.brsatalay.htmltablebuilder.model.enumeration.enmAlignment;
import com.brsatalay.htmltablebuilder.model.enumeration.enmCellValueType;
import com.brsatalay.htmltablebuilder.utils.UtilsDate;

import java.text.DecimalFormat;

public class mdlGridCell {
    private String fieldName;
    private int width;
    private String backgroundColor;
    private String textColor;
    private int fontSize;
    private String value;
    private String displayFormat;
    private enmAlignment alignment;
    private enmCellValueType valueType;

    private final String TEXT_INTEGER = "0";
    private final String TEXT_DOUBLE = "0.0";

    public mdlGridCell() {
        this.fieldName = "";
        this.backgroundColor = "white";
        this.textColor = "black";
        this.value = "";
        this.displayFormat = "";
        this.alignment = enmAlignment.LeftJustify;
        this.valueType = enmCellValueType.Unknow;
        this.fontSize = HtmlConstant.FontSize;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public enmAlignment getAlignment() {
        return alignment;
    }

    public void setAlignment(enmAlignment alignment) {
        this.alignment = alignment;
    }

    public String getDisplayFormat() {
        return displayFormat;
    }

    public void setDisplayFormat(String displayFormat) {
        this.displayFormat = displayFormat;
    }

    private String applyStyles(String value) {
        String tempValue = value;
        if (!(this instanceof mdlHeaderCell) && valueType != null) {
            switch (valueType) {
                case Double:
                    if (TextUtils.isEmpty(tempValue))
                        tempValue = "0.0";
                    break;
                case Decimal:
                    if (TextUtils.isEmpty(tempValue))
                        tempValue = "0.0";
                    if (!TextUtils.isEmpty(displayFormat)) {
                        Double decValue = new Double(tempValue);
                        DecimalFormat df = new DecimalFormat(displayFormat);
                        tempValue = df.format(decValue);
                    }
                    break;
                case Integer:
                    if (TextUtils.isEmpty(tempValue))
                        tempValue = "0";
                    break;

                case DateTime:
                    if (!TextUtils.isEmpty(tempValue)){
                        String pattern = displayFormat;
                        if (TextUtils.isEmpty(pattern))
                            pattern = FormatSettings.ShortDateFormat;
                        tempValue = UtilsDate.changeStrDatePattern(tempValue, FormatSettings.SqlDateTime, pattern);
                    }
                    break;
            }
        }
        return tempValue;
    }

    public enmCellValueType getValueType() {
        if (valueType == null)
            valueType = enmCellValueType.Unknow;
        return valueType;
    }

    public void setValueType(enmCellValueType valueType) {
        this.valueType = valueType;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFieldName() {
        if (fieldName == null)
            fieldName = "";
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String toString(){
        return applyStyles(value);
    }

    public Integer toInteger(){
        try {
            if (value == null)
                value = TEXT_INTEGER;
            else if (value.trim().isEmpty())
                value = TEXT_INTEGER;
            return Integer.valueOf(value.trim());
        }catch (Exception e){
            Log.e("mdlGridCell", " FieldName: "+ getFieldName() + " - " + e.getMessage());
            return 0;
        }
    }

    public Double toDouble(){
        try {
            if (value == null)
                value = TEXT_DOUBLE;
            else if (value.trim().isEmpty())
                value = TEXT_DOUBLE;
            return new Double(value.trim());
        }catch (Exception e){
            Log.e("mdlGridCell", " FieldName: "+ getFieldName() + " - " + e.getMessage());
            return new Double(0.0);
        }
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public boolean isValueDoubleOrDecimal() {
        return valueType == enmCellValueType.Double || valueType == enmCellValueType.Decimal;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }
}
