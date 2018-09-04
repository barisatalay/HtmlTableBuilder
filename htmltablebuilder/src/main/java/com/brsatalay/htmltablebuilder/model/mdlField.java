package com.brsatalay.htmltablebuilder.model;

import com.brsatalay.htmltablebuilder.model.enumeration.enmCellValueType;

public class mdlField {
    private String ColumnName;
    private String DataType;
    private String ColumnSize;
    private String isReadOnly;

    public String getColumnName() {
        return ColumnName;
    }

    public void setColumnName(String columnName) {
        ColumnName = columnName;
    }

    public enmCellValueType getDataType() {
        switch (DataType){
            case "String": return enmCellValueType.String;
            case "Integer": return enmCellValueType.Integer;
            case "Int16": return enmCellValueType.Integer;
            case "Int32": return enmCellValueType.Integer;
            case "Int64": return enmCellValueType.Integer;
            case "Double": return enmCellValueType.Double;
            case "Decimal": return enmCellValueType.Decimal;
            case "DateTime": return enmCellValueType.DateTime;
            default: return enmCellValueType.Unknow;
        }
    }

    public void setDataType(String dataType) {
        DataType = dataType;
    }

    public String getColumnSize() {
        return ColumnSize;
    }

    public void setColumnSize(String columnSize) {
        ColumnSize = columnSize;
    }

    public String getIsReadOnly() {
        return isReadOnly;
    }

    public void setIsReadOnly(String isReadOnly) {
        this.isReadOnly = isReadOnly;
    }

}
