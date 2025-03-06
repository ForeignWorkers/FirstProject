package VO;

import java.util.List;
import java.util.Map;

public class ItemVO {
    private int id;
    private String title;
    private String genres;
    private String url;
    private String promotionYear;
    private String promotionDay;
    private String nation;
    private String director;
    private String runningTime;
    private String ageRating;
    private String[] actor;
    private String thumbnail;
    private List<Map<String, String>> category;
    private String[] images;

    public ItemVO(int id,
                  String title,
                  String genres,
                  String url,
                  String promotionYear,
                  String promotionDay,
                  String nation,
                  String director,
                  String runningTime,
                  String ageRating,
                  String[] actor,
                  String thumbnail,
                  List<Map<String, String>> category,
                  String[] images) {
        this.id = id;
        this.title = title;
        this.genres = genres;
        this.url = url;
        this.promotionYear = promotionYear;
        this.promotionDay = promotionDay;
        this.nation = nation;
        this.director = director;
        this.runningTime = runningTime;
        this.ageRating = ageRating;
        this.actor = actor;
        this.thumbnail = thumbnail;
        this.category = category;
        this.images = images;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPromotionYear() {
        return promotionYear;
    }

    public void setPromotionYear(String promotionYear) {
        this.promotionYear = promotionYear;
    }

    public String getPromotionDay() {
        return promotionDay;
    }

    public void setPromotionDay(String promotionDay) {
        this.promotionDay = promotionDay;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getRunningTime() {
        return runningTime;
    }

    public void setRunningTime(String runningTime) {
        this.runningTime = runningTime;
    }

    public String getAgeRating() {
        return ageRating;
    }

    public void setAgeRating(String ageRating) {
        this.ageRating = ageRating;
    }

    public String[] getActor() {
        return actor;
    }

    public void setActor(String[] actor) {
        this.actor = actor;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<Map<String, String>> getCategory() {
        return category;
    }

    public void setCategory(List<Map<String, String>> category) {
        this.category = category;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }
}
