package com.example.viewpager.demain;


import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/15 0015.
 */
public class NewsData {
    public NewsMenuData showapi_res_body;

    @Override
    public String toString() {
        return "NewsData{" +
                "showapi_res_body=" + showapi_res_body +
                '}';
    }

    public class NewsMenuData {
        public ArrayList<NewsTabData> channelList;

        @Override
        public String toString() {
            return "NewsMenuData{" +
                    "channelList=" + channelList +
                    '}';
        }

        public class NewsTabData {
            public String name;
            public String channelId;
            @Override
            public String toString() {
                return "NewsTabData{" +
                        "name='" + name + '\'' +
                        '}';
            }
        }

    }

}
