package com.brsatalay.htmltablebuilder.model;


import com.brsatalay.htmltablebuilder.constant.HtmlConstant;
import com.brsatalay.htmltablebuilder.model.enumeration.enmAlignment;
import com.brsatalay.htmltablebuilder.model.enumeration.enmCellValueType;

public class mdlHeaderCell extends mdlGridCell {
    private int height;

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    private mdlHeaderCell(Builder builder) {
        super();
        setFieldName(builder.fieldName);
        setWidth(builder.width);
        setHeight(builder.height);
        setBackgroundColor(builder.backgroundColor);
        setTextColor(builder.textColor);
        setValue(builder.value);
        setDisplayFormat(builder.displayFormat);
        setAlignment(builder.alignment);
        setValueType(builder.valueType);
        setFontSize(builder.fontSize);
    }

    public static final class Builder {
        private String fieldName;
        private int width;
        private int height;
        private String backgroundColor;
        private String textColor;
        private String value;
        private String displayFormat;
        private enmAlignment alignment;
        private enmCellValueType valueType;
        private int fontSize;

        public Builder() {
            fontSize = HtmlConstant.FontSize;
        }

        public Builder fieldName(String val) {
            fieldName = val;
            return this;
        }

        public Builder width(int val) {
            width = val;
            return this;
        }

        public Builder height(int val) {
            height = val;
            return this;
        }

        public Builder backgroundColor(String val) {
            backgroundColor = val;
            return this;
        }

        public Builder textColor(String val) {
            textColor = val;
            return this;
        }

        public Builder value(String val) {
            value = val;
            return this;
        }

        public Builder displayFormat(String val) {
            displayFormat = val;
            return this;
        }

        public Builder alignment(enmAlignment val) {
            alignment = val;
            return this;
        }

        public Builder valueType(enmCellValueType val) {
            valueType = val;
            return this;
        }

        public Builder fontSize(int val) {
            fontSize = val;
            return this;
        }

        public mdlHeaderCell build() {
            return new mdlHeaderCell(this);
        }
    }
}
