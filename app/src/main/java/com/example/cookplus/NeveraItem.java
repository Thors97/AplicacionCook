package com.example.cookplus;

public class NeveraItem {
    private String text;
    private boolean checked;

    public NeveraItem(String text) {
        this.text = text;
        this.checked = false;
    }

    public NeveraItem(String text, boolean checked) {
        this.text = text;
        this.checked = checked;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void toggleChecked() {
        this.checked = !this.checked;
    }
}
