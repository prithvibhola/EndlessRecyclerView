package recyclerview.prithvi.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Prithvi on 8/23/2016.
 */
public class UnsplashData implements Parcelable {

    private String id, width, height, urlRegular;

    protected UnsplashData(Parcel in) {
        String[] data = new String[10];
        in.readStringArray(data);
        this.id = data[0];
        this.width = data[1];
        this.height = data[2];
        this.urlRegular = data[3];
    }

    public UnsplashData(){

    }

    public UnsplashData(String id, String width, String height, String urlRegular){
        this.id = id;
        this.width = width;
        this.height = height;
        this.urlRegular = urlRegular;
    }

    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return id;
    }

    public void setWidth(String width){
        this.width = width;
    }
    public String getWidth(){
        return width;
    }

    public void setHeight(String height){
        this.height = height;
    }
    public String getHeight(){
        return height;
    }

    public void setUrlRegular(String urlRegular){
        this.urlRegular = urlRegular;
    }
    public String getUrlRegular(){
        return urlRegular;
    }

    public static final Creator<UnsplashData> CREATOR = new Creator<UnsplashData>() {
        @Override
        public UnsplashData createFromParcel(Parcel in) {
            return new UnsplashData(in);
        }

        @Override
        public UnsplashData[] newArray(int size) {
            return new UnsplashData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(width);
        parcel.writeString(height);
        parcel.writeString(urlRegular);
    }
}
