package com.santoshdhakal.internshipchallenge.models;

import android.arch.persistence.room.ColumnInfo;

public class CompanyModel {
    @ColumnInfo(name = "company_name")
    private String name;

    @ColumnInfo(name = "catch_phrase")
    private String catchPhrase;

    private String bs;

    public CompanyModel(String name, String catchPhrase, String bs) {
        this.name = name;
        this.catchPhrase = catchPhrase;
        this.bs = bs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatchPhrase() {
        return catchPhrase;
    }

    public void setCatchPhrase(String catchPhrase) {
        this.catchPhrase = catchPhrase;
    }

    public String getBs() {
        return bs;
    }

    public void setBs(String bs) {
        this.bs = bs;
    }
}
