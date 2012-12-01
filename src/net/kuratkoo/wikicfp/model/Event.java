package net.kuratkoo.wikicfp.model;

import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.kuratkoo.wikicfp.tools.Tools;

/**
 * Event
 *
 * @author Radim -kuratkoo- Vaculik <kuratkoo@gmail.com>
 */
public class Event {

    private static final String TAG = "WikiCFP|Event";
    private String id;
    private String title;
    private String subtitle;
    private String location;
    private String deadline;
    private String when;
    private String notification;
    private String finalVersion;
    private String link;
    private ArrayList<Category> categories;
    private String description;
    private ArrayList<Event> related;

    public Event() {
        categories = new ArrayList<Category>();
        related = new ArrayList<Event>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Event> getRelated() {
        return related;
    }

    public void setRelated(ArrayList<Event> related) {
        this.related = related;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getFinalVersion() {
        return finalVersion;
    }

    public void setFinalVersion(String finalVersion) {
        this.finalVersion = finalVersion;
    }

    public void load() throws IOException {
        StringBuilder content = Tools.getContent("http://wikicfp.com/cfp/servlet/event.showcfp?eventid=" + this.getId());

        Pattern p;
        Matcher m;

        p = Pattern.compile("&nbsp;&nbsp; <a href=\"[^\"]+\">([^<]+)</a>");
        m = p.matcher(content);
        while (m.find()) {
            MatchResult mr = m.toMatchResult();
            Category c = new Category();
            c.setName(mr.group(1));
            categories.add(c);
        }

        p = Pattern.compile("<a href=\"/cfp/servlet/event.showcfp\\?eventid=([0-9]+)\">([^<]+)</a>&nbsp;&nbsp;([^<]+)</td>");
        m = p.matcher(content.toString().replace("\n", ""));
        while (m.find()) {
            MatchResult mr = m.toMatchResult();
            Event e = new Event();
            e.setId(mr.group(1));
            e.setTitle(mr.group(2));
            e.setSubtitle(mr.group(3));
            related.add(e);
        }

        p = Pattern.compile("Link: <a href=\"([^\"]+)\"");
        m = p.matcher(content);
        while (m.find()) {
            MatchResult mr = m.toMatchResult();
            setLink(mr.group(1));
        }

        p = Pattern.compile("<div class=\"cfp\" align=\"left\">(.*)</div></td></tr></table>");
        m = p.matcher(content.toString().replace("\n", ""));
        while (m.find()) {
            MatchResult mr = m.toMatchResult();
            setDescription(mr.group(1).replace("<br>", "\n"));
        }

        p = Pattern.compile("content=\"Notification Due\"></span>                    <span resource=\"[^\"]+\" rel=\"v:url\"></span>                    <span property=\"v:startDate\" content=\"[^\"]+\">([^<]+)</span>");
        m = p.matcher(content.toString().replace("\n", ""));
        while (m.find()) {
            MatchResult mr = m.toMatchResult();
            setNotification(mr.group(1));
        }

        p = Pattern.compile("content=\"Final Version Due\"></span>                    <span resource=\"[^\"]+\" rel=\"v:url\"></span>                    <span property=\"v:startDate\" content=\"[^\"]+\">([^<]+)</span>");
        m = p.matcher(content.toString().replace("\n", ""));
        while (m.find()) {
            MatchResult mr = m.toMatchResult();
            setFinalVersion(mr.group(1));
        }

    }
}
