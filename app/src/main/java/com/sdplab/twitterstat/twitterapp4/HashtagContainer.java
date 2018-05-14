package com.sdplab.twitterstat.twitterapp4;

import java.util.ArrayList;

public class HashtagContainer {

    private ArrayList<String> tagList;

    private static HashtagContainer instance;

    private HashtagContainer(){
        tagList = new ArrayList<String>();
    }

    public static HashtagContainer getInstance(){
        if(instance==null){
            synchronized (HashtagContainer.class){
                if(instance==null){
                    instance = new HashtagContainer();
                }
            }
        }
        return instance;
    }

    public void addTag(String tag){
        tagList.add(tag);
    }

    public ArrayList<String> getTagList(){
        return tagList;
    }

}
