package com.brsatalay.htmltablebuilder.model;

import java.util.LinkedHashMap;

public interface HtmlTableBuilderListener {
    void onDrawCell(int dataIndex, mdlGridCell cell);
    /**
     * Geliştirmesine başlanmadı
     * */
    void onDrawColumnHeader(int dataIndex, mdlGridCell cell);

    void onCalcFields(int rowIndex, LinkedHashMap<String, mdlGridCell> rowData);
}
