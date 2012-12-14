package net.kuratkoo.wikicfp.model;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.kuratkoo.wikicfp.tools.Tools;

/**
 * EventSet
 * 
 * @author Radim -kuratkoo- Vaculik <kuratkoo@gmail.com>
 */
public class EventSet extends ArrayList<Event> {

    private static final String TAG = "WikiCFP|EventSet";

    public void load(String category) throws IOException {
        StringBuilder content = Tools.getContent("http://www.wikicfp.com/cfp/call?conference=" + category.replace(" ", "%20"));
        Pattern p = Pattern.compile("<td rowspan=\"2\" align=\"left\"><a href=\"[^\"]+\">([^<]+)</a></td><td align=\"left\" colspan=\"3\">([^<]+)</td><td align=\"center\" rowspan=\"2\"><input type=\"checkbox\" name=\"eventid_([0-9]+)\"></td></tr><tr bgcolor=\"[^\"]+\"><td align=\"left\">([^<]+)</td><td align=\"left\">([^<]+)</td><td align=\"left\">([^<]+)</td></tr>");
        Matcher m = p.matcher(content.toString().replace("\n", ""));
        while (m.find()) {
            MatchResult mr = m.toMatchResult();
            Event e = new Event();
            e.setTitle(mr.group(1));
            e.setSubtitle(mr.group(2));
            e.setId(mr.group(3));
            e.setWhen(mr.group(4));
            e.setLocation(mr.group(5));
            e.setDeadline(mr.group(6));
            this.add(e);
        }
    }    

    public void search(String search) throws IOException {
        search = URLEncoder.encode(search, "UTF-8");
        StringBuilder content = Tools.getContent("http://wikicfp.com/cfp/servlet/tool.search?q=" + search + "&year=a");
        Pattern p = Pattern.compile("<td rowspan=\"2\" align=\"left\"><a href=\"/cfp/servlet/event.showcfp\\?eventid=([0-9]+)&amp;copyownerid=[0-9]+\">([^<]+)</a></td><td align=\"left\" colspan=\"3\">([^<]+)</td></tr><tr bgcolor=\"[^\"]+\"><td align=\"left\">([^<]+)</td><td align=\"left\">([^<]+)</td><td align=\"left\">([^<]+)</td></tr>");
        Matcher m = p.matcher(content.toString().replace("\n", ""));
        while (m.find()) {
            MatchResult mr = m.toMatchResult();
            Event e = new Event();
            e.setId(mr.group(1));
            e.setTitle(mr.group(2));
            e.setSubtitle(mr.group(3));
            e.setWhen(mr.group(4));
            e.setLocation(mr.group(5));
            e.setDeadline(mr.group(6));
            this.add(e);
        }
    }  
}
