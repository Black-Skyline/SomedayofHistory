package com.example.internetapiexample;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class DataTransmission implements Serializable {
    private String month = null;

    @SerializedName(value = "", alternate = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"})
    FirstPod firstPod;

    public static class FirstPod {
        private String date = null;

        @SerializedName(value = "", alternate = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"})
        SecondPod secondPod;

        public static class SecondPod {
            private String year;
            private String title;
            private String simple_introduction;
            private String url_link;

            public void setYear(String year) {
                this.year = year;
            }

            public String getYear() {
                return year;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getTitle() {
                return title;
            }

            public void setSimple_introduction(String simple_introduction) {
                this.simple_introduction = simple_introduction;
            }

            public String getSimple_introduction() {
                return simple_introduction;
            }

            public void setUrl_link(String url_link) {
                this.url_link = url_link;
            }

            public String getUrl_link() {
                return url_link;
            }
        }
    }
}
