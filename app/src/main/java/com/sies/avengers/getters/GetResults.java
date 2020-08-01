package com.sies.avengers.getters;


import java.util.List;

public class GetResults {

    private GetThumbnail thumbnail;
    private String description;
    private String title;
    private Integer id;
    private String name;
    private List<GetUrls> urls;

    public List<GetUrls> getUrls() {
        return urls;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public int getId(){
        return id;
    }

    public GetThumbnail getThumbnail() {
        return thumbnail;
    }

    public String getDescription() {
        return description;
    }



}
