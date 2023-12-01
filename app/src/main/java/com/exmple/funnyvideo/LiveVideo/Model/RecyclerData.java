package com.exmple.funnyvideo.LiveVideo.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecyclerData {

    @SerializedName("Total Records")
    @Expose
    private int TotalRecords;
    @SerializedName("Records/page")
    @Expose
    private int recordperpage;
    @SerializedName("Total Pages")
    @Expose
    private int Totalpages;
    @SerializedName("Current Page")
    @Expose
    private int Currentpages;
    @SerializedName("Data")
    @Expose
    private List<Data> dataArray = null;

    public int getTotalRecords() {
        return TotalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        TotalRecords = totalRecords;
    }

    public int getRecordperpage() {
        return recordperpage;
    }

    public void setRecordperpage(int recordperpage) {
        this.recordperpage = recordperpage;
    }

    public int getTotalpages() {
        return Totalpages;
    }

    public void setTotalpages(int totalpages) {
        Totalpages = totalpages;
    }

    public int getCurrentpages() {
        return Currentpages;
    }

    public void setCurrentpages(int currentpages) {
        Currentpages = currentpages;
    }

    public List<Data> getDataArray() {
        return dataArray;
    }

    public void setDataArray(List<Data> dataArray) {
        this.dataArray = dataArray;
    }
}
