package com.brsatalay.htmltablebuilder.model;

public class StringWrapper {
    private String value;

    public StringWrapper(String value) {
        this.value = value;
    }

    public String toString() {
        clearIsNullText();
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public StringWrapper add(String newText) {
        this.value = this.value + newText;
        return this;
    }

    public void clearIsNullText(){
        if (value == null)
            value = "";
        else if(value.trim().equalsIgnoreCase("null"))
            value = "";
    }
}
