package com.xiaofutest.model;

/**
 * @author :xs
 * @date : 2024/9/25
 */

public class LogisticsDTO {

    // 文案
    private String title;

    // 年限
    private String year;

    // 作者
    private String author;

    // 主角
    private String protagonist;

    public LogisticsDTO() {
    }

    public LogisticsDTO(String title, String year, String author, String protagonist) {
        this.title = title;
        this.year = year;
        this.author = author;
        this.protagonist = protagonist;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }
    public void setYear(String year) {
        this.year = year;
    }

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    public String getProtagonist() {
        return protagonist;
    }

    public void setProtagonist(String protagonist) {
        this.protagonist = protagonist;
    }
}
