package com.contus.mypolls;

import java.io.File;

/**
 * Created by Techuva on 25-11-2017.
 */

public class ImageModel {
    String  type;
    File    file;
    String   url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }


}
