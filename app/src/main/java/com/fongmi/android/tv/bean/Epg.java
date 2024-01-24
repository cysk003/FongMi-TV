package com.fongmi.android.tv.bean;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.fongmi.android.tv.App;
import com.fongmi.android.tv.R;
import com.fongmi.android.tv.utils.ResUtil;
import com.fongmi.android.tv.utils.Util;
import com.github.catvod.utils.Trans;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

public class Epg {

    @SerializedName("date")
    private String date;
    @SerializedName("epg_data")
    private List<Epg> list;
    @SerializedName("title")
    private String title;
    @SerializedName("start")
    private String start;
    @SerializedName("end")
    private String end;
    @SerializedName("key")
    private String key;

    private long startTime;
    private long endTime;

    public static Epg objectFrom(String str, String key, SimpleDateFormat format) {
        try {
            Epg item = App.gson().fromJson(str, Epg.class);
            item.setTime(format);
            item.setKey(key);
            return item;
        } catch (Exception e) {
            return new Epg();
        }
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDate() {
        return TextUtils.isEmpty(date) ? "" : date;
    }

    public List<Epg> getList() {
        return list == null ? Collections.emptyList() : list;
    }

    public void setList(List<Epg> list) {
        this.list = list;
    }

    public String getTitle() {
        return TextUtils.isEmpty(title) ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStart() {
        return TextUtils.isEmpty(start) ? "" : start;
    }

    public String getEnd() {
        return TextUtils.isEmpty(end) ? "" : end;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public boolean equal(String date) {
        return getDate().equals(date);
    }

    private void setTime(SimpleDateFormat format) {
        setList(new ArrayList<>(new LinkedHashSet<>(getList())));
        for (Epg item : getList()) {
            item.setStartTime(Util.format(format, getDate().concat(item.getStart())));
            item.setEndTime(Util.format(format, getDate().concat(item.getEnd())));
            item.setTitle(Trans.s2t(item.getTitle()));
        }
    }

    public boolean isInRange() {
        return getStartTime() <= System.currentTimeMillis() && System.currentTimeMillis() <= getEndTime();
    }

    private String format() {
        if (getTitle().isEmpty()) return "";
        if (getStart().isEmpty() && getEnd().isEmpty()) return ResUtil.getString(R.string.play_now, getTitle());
        return getStart() + " ~ " + getEnd() + "  " + getTitle();
    }

    public String getTime() {
        if (getStart().isEmpty() && getEnd().isEmpty()) return "";
        return getStart() + " ~ " + getEnd();
    }

    public String getEpg() {
        for (Epg item : getList()) if (item.isInRange()) return item.format();
        return "";
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Epg)) return false;
        Epg it = (Epg) obj;
        return getTitle().equals(it.getTitle()) && getEnd().equals(it.getEnd()) && getStart().equals(it.getStart());
    }

    @Override
    public int hashCode() {
        int result = getTitle().hashCode();
        result = 31 * result + getEnd().hashCode();
        result = 31 * result + getStart().hashCode();
        return result;
    }
}
