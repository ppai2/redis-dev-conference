package ch3;

import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;



/**
 * Created by tague on 3/12/17.
 */
public class ViewedItem
{
    @SerializedName("date")
    public Date date;

    @SerializedName("title")
    public String title;

    @SerializedName("url")
    public String url;

    public ViewedItem(Date date, String title, String url) {
        this.date = date;
        this.title = title;
        this.url = url;
    }

    public String to_serialized_json() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static ViewedItem from_serialized_json(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, ViewedItem.class);
    }
}
