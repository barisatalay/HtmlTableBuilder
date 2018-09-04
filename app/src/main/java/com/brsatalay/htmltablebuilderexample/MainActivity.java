package com.brsatalay.htmltablebuilderexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.brsatalay.htmltablebuilder.HtmlTableBuilder;
import com.brsatalay.htmltablebuilder.constant.FormatSettings;
import com.brsatalay.htmltablebuilder.model.HtmlTableBuilderListener;
import com.brsatalay.htmltablebuilder.model.enumeration.enmAlignment;
import com.brsatalay.htmltablebuilder.model.enumeration.enmCellValueType;
import com.brsatalay.htmltablebuilder.model.enumeration.enmFooter;
import com.brsatalay.htmltablebuilder.model.mdlFooterCell;
import com.brsatalay.htmltablebuilder.model.mdlGridCell;
import com.brsatalay.htmltablebuilder.model.mdlHeaderCell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements HtmlTableBuilderListener {

    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webView);

        HtmlTableBuilder tableBuilder = new HtmlTableBuilder(getApplicationContext(), true);
        tableBuilder.setNoDataText("No Data");
        tableBuilder.setOutOfMemoryText("Due to excessive data, report can not be displayed. Please try again with more applicable search criteria.");
        tableBuilder.setHeaderInfos(prepareHeaders());
        tableBuilder.setFooterInfos(prepareFooterList());
        tableBuilder.setDataSet(prepareDataSet());
        tableBuilder.setListener(this);
        String htmlPath = tableBuilder.build("table.html");


//        webView.getSettings().setAllowFileAccess(true);
//
//        webView.clearCache(true);
//        webView.clearHistory();
//        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
//        webView.getSettings().setSaveFormData(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        webView.loadUrl("file:///" + htmlPath);
    }

    private List prepareDataSet() {
        List<Stock> result = new ArrayList<>();
        Stock stock = new Stock();
        stock.setId(1);
        stock.setName("Pencil");
        stock.setPrice(getRandomDouble());//new Double(534534.05));
        stock.setCrtDate("2018-07-15T22:55:00");
        result.add(stock);

        stock = new Stock();
        stock.setId(2);
        stock.setName("Notebook");
        stock.setPrice(new Double(154562.75));
        stock.setCrtDate("2018-06-11T12:32:00");
        result.add(stock);

        stock = new Stock();
        stock.setId(3);
        stock.setName("Umbrella");
        stock.setPrice(new Double(34123.00));
        stock.setCrtDate("2018-04-30T08:07:00");
        result.add(stock);

        stock = new Stock();
        stock.setId(4);
        stock.setName("Headphones");
        stock.setPrice(new Double(11231234.06));
        stock.setCrtDate("2018-05-25T17:30:00");
        result.add(stock);

        stock = new Stock();
        stock.setId(5);
        stock.setName("Table");
        stock.setPrice(new Double(567434.06));
        stock.setCrtDate("2018-03-11T17:30:00");
        result.add(stock);

        stock = new Stock();
        stock.setId(6);
        stock.setName("Monitor");
        stock.setPrice(new Double(8347521578.99));
        stock.setCrtDate("2017-01-01T08:30:00");
        result.add(stock);

        stock = new Stock();
        stock.setId(7);
        stock.setName("Cellphone");
        stock.setPrice(new Double(11231234.06));
        stock.setCrtDate("2016-05-25T17:30:00");
        result.add(stock);

        stock = new Stock();
        stock.setId(8);
        stock.setName("Coffe");
        stock.setPrice(new Double(11231234.06));
        stock.setCrtDate("2017-04-23T17:30:00");
        result.add(stock);

        stock = new Stock();
        stock.setId(9);
        stock.setName("Glass");
        stock.setPrice(new Double(11231234.06));
        stock.setCrtDate("2020-08-08T17:30:00");
        result.add(stock);

        stock = new Stock();
        stock.setId(10);
        stock.setName("Desk");
        stock.setPrice(new Double(11231234.06));
        stock.setCrtDate("2019-12-07T17:30:00");
        result.add(stock);

        return result;
    }

    private Double getRandomDouble(){
        double start = 112313321.99;
        double end =   987989879879.99;
        double random = new Random().nextDouble();
        return start + (random * (end - start));
    }

    private LinkedHashMap<String, mdlFooterCell> prepareFooterList() {
        LinkedHashMap<String, mdlFooterCell> footerInfos = new LinkedHashMap<>();

        footerInfos.put("id", new mdlFooterCell.Builder()
                .type(enmFooter.Count)
                .build());

        footerInfos.put("price", new mdlFooterCell.Builder()
                .type(enmFooter.Sum)
                .build());

        return footerInfos;
    }

    private LinkedHashMap<String, mdlHeaderCell> prepareHeaders() {
        LinkedHashMap<String, mdlHeaderCell> headerInfos = new LinkedHashMap<>();

        headerInfos.put("id", new mdlHeaderCell.Builder()
                .value(getString(R.string.id))
                .alignment(enmAlignment.LeftJustify)
                .valueType(enmCellValueType.Integer)
                .backgroundColor("red")
                .textColor("white")
                .width(110)
                .build());

        headerInfos.put("name", new mdlHeaderCell.Builder()
                .value(getString(R.string.name))
                .alignment(enmAlignment.LeftJustify)
                .valueType(enmCellValueType.String)
                .fontSize(25)
                .width(170)
                .build());

        headerInfos.put("price", new mdlHeaderCell.Builder()
                .value(getString(R.string.price))
                .alignment(enmAlignment.RightJustify)
                .displayFormat(FormatSettings.DecimalFormat)
                .valueType(enmCellValueType.Decimal)
                .width(175)
                .build());

        headerInfos.put("crtDate", new mdlHeaderCell.Builder()
                .value(getString(R.string.create_date))
                .alignment(enmAlignment.LeftJustify)
                .displayFormat(FormatSettings.ShortDateFormat)
                .valueType(enmCellValueType.DateTime)
                .width(110)
                .build());
        return headerInfos;
    }

    @Override
    public void onDrawCell(int dataIndex, mdlGridCell cell) {
        boolean isMoneyCol = cell.getValueType() == enmCellValueType.Decimal;

        if ((dataIndex % 2) == 0){
            cell.setBackgroundColor(!isMoneyCol ? "#F2F7FF" : "#B3D1B3");
        }else if (!isMoneyCol)
            cell.setBackgroundColor("white");
        else
            cell.setBackgroundColor("#C0DCC0");
    }

    @Override
    public void onDrawColumnHeader(int dataIndex, mdlGridCell cell) {

    }

    @Override
    public void onCalcFields(int rowIndex, LinkedHashMap<String, mdlGridCell> rowData) {

    }
}
