package com.nhpatt.rxjava;

import java.util.List;

public class SearchResult {

    private List<Talk> documents;
    private int total;

    public List<Talk> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Talk> documents) {
        this.documents = documents;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
