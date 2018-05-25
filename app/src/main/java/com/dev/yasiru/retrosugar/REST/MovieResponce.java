package com.dev.yasiru.retrosugar.REST;

import com.dev.yasiru.retrosugar.DB.Movie;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Shalu on 1/25/2017.
 */
public class MovieResponce {


    private int page;

    private List<Movie> results;

    private int totalResults;

    private int totalPages;



    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
