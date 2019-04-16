package com.android.instaposts;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageMetadata {

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:sss'Z'";
    public static final String EXPRESSION = "#(\\S+)";
    private final String caption;
    private final String url;
    private final User user;
    private final String uploadTime;
    private final List<String> hashtags;

    public ImageMetadata() {
        this.caption = "";
        this.url = "";
        this.uploadTime = new SimpleDateFormat(DATE_FORMAT).format(new Date());
        this.hashtags = new ArrayList<>();
        this.user = null;
    }

    public ImageMetadata(String caption, String url, User user) {
        this.caption = caption;
        this.url = url;
        this.uploadTime = new SimpleDateFormat(DATE_FORMAT).format(new Date());
        this.user = user;
        this.hashtags = getHashtags(this.caption);
    }

    public String getCaption() {
        return caption;
    }

    public String getUrl() {
        return url;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    private List<String> getHashtags(String caption) {
        Pattern pattern = Pattern.compile(EXPRESSION);
        Matcher mat = pattern.matcher(caption);
        List<String> hashtags = new ArrayList<>();
        while (mat.find()) {
            hashtags.add(mat.group(1));
        }
        return hashtags;
    }

    public User getUser() {
        return user;
    }

    public List<String> getHashtags() {
        return this.hashtags;
    }
}
