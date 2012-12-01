package net.kuratkoo.wikicfp.model;

import android.content.Context;
import android.content.SharedPreferences;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.kuratkoo.wikicfp.tools.Tools;

/**
 * CategorySet
 *
 * @author Radim -kuratkoo- Vaculik <kuratkoo@gmail.com>
 */
public class CategorySet extends ArrayList<Category> {

    private static final String TAG = "WikiCFP|CategorySet";
    private Context mContext;

    public CategorySet(Context context) {
        this.mContext = context;
    }
    
    public void load() throws IOException {
        SharedPreferences sharedPref = mContext.getSharedPreferences("star", 0);
        StringBuilder content = Tools.getContent("http://www.wikicfp.com/cfp/allcat?sortby=1");
        Pattern p = Pattern.compile("<td><a href=\"/cfp/call\\?conference=[^\"]+\">([^<]+)</a></td><td align=\"center\">([0-9]+)</td>");
        Matcher m = p.matcher(content);
        while (m.find()) {
            MatchResult mr = m.toMatchResult();
            Category c = new Category();
            c.setName(mr.group(1));
            c.setCount(mr.group(2));
            c.setStar(sharedPref.getBoolean(mr.group(1), false));
            this.add(c);
        }
        this.sort();

    }
    
    public void sort() {
        Collections.sort(this, new Comparator<Category>() {
            public int compare(Category c1, Category c2) {
                return c1.getName().toLowerCase().compareTo(c2.getName().toLowerCase());
            }
        });
        
        Collections.sort(this, new Comparator<Category>() {
            public int compare(Category c1, Category c2) {
                return c2.getStar().compareTo(c1.getStar());
            }
        });        
    }
}
