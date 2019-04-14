package com.android.instaposts;

import java.util.List;

public class HashtagMetadata {

    private List<ImageMetadata> images;

    public HashtagMetadata(List<ImageMetadata> images) {
        this.images = images;
    }

    public List<ImageMetadata> getImages() {
        return images;
    }
}
