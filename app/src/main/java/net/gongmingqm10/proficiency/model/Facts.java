package net.gongmingqm10.proficiency.model;

import java.io.Serializable;

public class Facts implements Serializable {
    private String title;
    private Item[] rows;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Item[] getRows() {
        return rows;
    }

    public void setRows(Item[] rows) {
        this.rows = rows;
    }
}
