package com.example.petitspapiers.objects;

public class ResultModel {

    private final String titleFr;
    private final String titleVo;
    private final String image;
    private final String infos;
    private final String originalLanguage;
    private final String date;
    private final String genres;

    public ResultModel(String titleFr, String titleVo, String image, String infos, String originalLanguage, String date, String genres) {
        this.titleFr = titleFr;
        this.titleVo = titleVo;
        this.image = image;
        this.infos = infos;
        this.originalLanguage = originalLanguage;
        this.date = date;
        this.genres = genres;
    }

    public String getTitleFr() {
        return titleFr;
    }

    public String getTitleVo() {
        return titleVo;
    }

    public String getImage() {
        return image;
    }

    public String getInfos() {
        return infos;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getDate() {
        return date;
    }

    public String getGenres() {
        return genres;
    }

    @Override
    public String toString() {
        return "ResultModel{" +
                "titleFr='" + titleFr + '\'' +
                ", titleVo='" + titleVo + '\'' +
                ", image='" + image + '\'' +
                ", infos='" + infos + '\'' +
                ", originalLanguage='" + originalLanguage + '\'' +
                '}';
    }
}
