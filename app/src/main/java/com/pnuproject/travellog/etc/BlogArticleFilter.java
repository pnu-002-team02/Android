package com.pnuproject.travellog.etc;

import com.pnuproject.travellog.Main.MapFragment.Controller.Model.ResponseDataBlog;

import java.util.Vector;

public class BlogArticleFilter {
    Vector<String> vBlockWord;
    int blockWordSize;
    public BlogArticleFilter() {
        vBlockWord = new Vector<String>();
        vBlockWord.add("부동산");
        vBlockWord.add("카페");
        vBlockWord.add("맛");
        vBlockWord.add("갈비");
        vBlockWord.add("아파트");
        vBlockWord.add("술");
        vBlockWord.add("커피");
        vBlockWord.add("물회");
        vBlockWord.add("수익");

        blockWordSize = vBlockWord.size();
    }
    public int applyFilter(Vector<ResponseDataBlog.BlogDocuments> vArticle) {
        int i = 0 ;
        while( i < vArticle.size() ) {
            ResponseDataBlog.BlogDocuments blogDocument = vArticle.get(i);
            String contents = blogDocument.getContents();
            String title = blogDocument.getTitle();
            if (blogDocument.getThumbnail_url() == null || blogDocument.getThumbnail_url().isEmpty()) {
                vArticle.remove(i);
                continue;
            }
            if( isTrashArticle(contents, title) ) {
                vArticle.remove(i);
                continue;
            }

            title = removeTag(title);
            contents = removeTag(contents);
            blogDocument.setContents(contents);
            blogDocument.setTitle(title);
            i= i+1;
        }

        return vArticle.size();
    }

    public boolean isTrashArticle(String article,String title) {
        for(int i = 0 ; i < blockWordSize ; i++) {
            if( article.contains(vBlockWord.get(i))) {
                return true;
            }
        }

        return false;
    }

    public String removeTag(String article) {
        article = article.replaceAll("<b>","");
        article = article.replaceAll("</b>","");
        article = article.replaceAll("&lt;","");
        article = article.replaceAll("&gt;","");
        article = article.replaceAll("&#34;","");
        article = article.replaceAll("&#39;","");
        return article;
    }
}
