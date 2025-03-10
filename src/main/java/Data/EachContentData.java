package Data;

import javax.swing.*;
import java.awt.*;

public class EachContentData {
    private ImageIcon bgImage;
    private Rectangle bgRect;

    private ImageIcon thumbImage;
    private Rectangle thumbRect;

    private int constants;          //CENTER = 0, TOP = 1, LEFT = 2, BOTTOM = 3, RIGHT = 4
    private Rectangle titleRect;
    private int titleSize;

    private ImageIcon ratingIcon;
    private Rectangle ratingRect;
    private Rectangle ratingTextRect;

    private int ratingTextSize;

    private Rectangle categoryTextRect;
    private int categoryTextSize;
    private Rectangle categoryBgRect;

    public Rectangle getCategoryBgRect() {
        return categoryBgRect;
    }

    public void setCategoryBgRect(Rectangle categoryBgRect) {
        this.categoryBgRect = categoryBgRect;
    }

    public int getTitleSize() {
        return titleSize;
    }

    public Rectangle getCategoryTextRect() {
        return categoryTextRect;
    }

    public void setCategoryTextRect(Rectangle categoryTextRect) {
        this.categoryTextRect = categoryTextRect;
    }

    public void setTitleSize(int titleSize) {
        this.titleSize = titleSize;
    }

    public int getRatingTextSize() {
        return ratingTextSize;
    }

    public void setRatingTextSize(int ratingTextSize) {
        this.ratingTextSize = ratingTextSize;
    }

    public ImageIcon getRatingIcon() {
        return ratingIcon;
    }

    public void setRatingIcon(ImageIcon ratingIcon) {
        this.ratingIcon = ratingIcon;
    }

    public int getCategoryTextSize() {
        return categoryTextSize;
    }

    public void setCategoryTextSize(int categoryTextSize) {
        this.categoryTextSize = categoryTextSize;
    }

    public EachContentData(ImageIcon bgImage,
                           Rectangle bgRect,
                           ImageIcon thumbImage,
                           Rectangle thumbRect,
                           int constants,
                           Rectangle titleRect,
                           int titleSize,
                           ImageIcon ratingIcon,
                           Rectangle ratingRect,
                           Rectangle ratingTextRect,
                           int ratingTextSize,
                           Rectangle categoryTextRect,
                           int categoryTextSize,
                           Rectangle categoryBgRect
                           ) {
        this.bgImage = bgImage;
        this.bgRect = bgRect;
        this.thumbImage = thumbImage;
        this.thumbRect = thumbRect;
        this.constants = constants;
        this.titleRect = titleRect;
        this.titleSize = titleSize;
        this.ratingRect = ratingRect;
        this.ratingTextRect = ratingTextRect;
        this.ratingTextSize = ratingTextSize;
        this.categoryTextRect = categoryTextRect;
        this.categoryBgRect = categoryBgRect;
        this.categoryTextSize = categoryTextSize;
        this.ratingIcon = ratingIcon;
    }

    public ImageIcon getBgImage() {
        return bgImage;
    }

    public void setBgImage(ImageIcon bgImage) {
        this.bgImage = bgImage;
    }

    public Rectangle getBgRect() {
        return bgRect;
    }

    public void setBgRect(Rectangle bgRect) {
        this.bgRect = bgRect;
    }

    public ImageIcon getThumbImage() {
        return thumbImage;
    }

    public void setThumbImage(ImageIcon thumbImage) {
        this.thumbImage = thumbImage;
    }

    public Rectangle getThumbRect() {
        return thumbRect;
    }

    public void setThumbRect(Rectangle thumbRect) {
        this.thumbRect = thumbRect;
    }

    public int getConstants() {
        return constants;
    }

    public void setConstants(int constants) {
        this.constants = constants;
    }

    public Rectangle getTitleRect() {
        return titleRect;
    }

    public void setTitleRect(Rectangle titleRect) {
        this.titleRect = titleRect;
    }

    public Rectangle getRatingRect() {
        return ratingRect;
    }

    public void setRatingRect(Rectangle ratingRect) {
        this.ratingRect = ratingRect;
    }

    public Rectangle getRatingTextRect() {
        return ratingTextRect;
    }

    public void setRatingTextRect(Rectangle ratingTextRect) {
        this.ratingTextRect = ratingTextRect;
    }
}
