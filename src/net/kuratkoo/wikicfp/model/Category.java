package net.kuratkoo.wikicfp.model;

/**
 * Category
 * 
 * @author Radim -kuratkoo- Vaculik <kuratkoo@gmail.com>
 */
public class Category {
    private static final String TAG = "WikiCFP|Category";

    private String name;
    private String count;
    private Boolean star;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getStar() {
        return star;
    }

    public void setStar(Boolean star) {
        this.star = star;
    }
}
