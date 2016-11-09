package com.example.viewpager.demain;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/18 0018.   页签详情页数据
 */
public class TabData {
    public TabDataile showapi_res_body;

    @Override
    public String toString() {
        return "TabData{" +
                "showapi_res_body=" + showapi_res_body +
                '}';
    }

    public class TabDataile {
        public TapDataConteng pagebean;

        @Override
        public String toString() {
            return "TabDataile{" +
                    "pagebean=" + pagebean +
                    '}';
        }

        public class TapDataConteng {
            public ArrayList<ChildContent> contentlist;

            @Override
            public String toString() {
                return "TapDataConteng{" +
                        "contentlist=" + contentlist +
                        '}';
            }

            public  class ChildContent{
                @Override
                public String toString() {
                    return "ChildContent{" +
                            "desc='" + desc + '\'' +
                            ", link='" + link + '\'' +
                            ", pubData='" + pubDate + '\'' +
                            ", source='" + source + '\'' +
                            ", title='" + title + '\'' +
                            '}';
                }

                public String desc;
                public String link;
                public String pubDate;
                public String source;
                public String title;
            }
        }
    }
}
