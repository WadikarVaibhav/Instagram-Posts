package com.android.instaposts;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageMetadata {

    final String caption;
    final String url;
    final User user;
    final String uploadTime;
    final List<String> hashtags;

    public ImageMetadata() {
        this.caption = "";
        this.url = "";
        this.uploadTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss'Z'").format(new Date());
        this.hashtags = new ArrayList<>();
        this.user = null;
    }

    public ImageMetadata(String caption, String url, User user) {
        this.caption = caption;
        this.url = url;
        this.uploadTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss'Z'").format(new Date());
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
        Pattern pattern = Pattern.compile("#(\\S+)");
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
