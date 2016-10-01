package com.zlion.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by zzs on 2016/8/31.
 * This is a sample model
 */
@Entity
@Table(name = "article", schema = "blog", catalog = "")
public class Article implements Serializable{

    @Id
    @GeneratedValue
    private Long id;

    @Basic
    @Column(name = "title", unique = true, nullable = false)
    private String title;

    @Basic
    @Column(name = "content")
    private String content;

    public Article(){}

    public Article(String title, String content){
        this.title = title;
        this.content = content;
    }


    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
