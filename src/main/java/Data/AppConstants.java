package Data;

public final class AppConstants {
    // private 생성자로 인스턴스화 방지
    private AppConstants() {
        throw new UnsupportedOperationException("상수는 private 만들수 없습니다.");
    }

    public static final int FRAME_HEIGHT = 735;
    public static final int FRAME_WIDTH = 595;

    public static final int PANEL_UP_WIDTH = 595;
    public static final int PANEL_UP_HEIGHT = 83;

    public static final int PANEL_MID_WIDTH = 595;
    public static final int PANEL_MID_HEIGHT = 549;

    public static final int PANEL_DOWN_WIDTH = 595;
    public static final int PANEL_DOWN_HEIGHT = 99;

    public static final int STAR_MAX_SCORE = 5;
    public static final int MAXIMUM_REVIEW_TEXT = 120;
    public static final int MINUMUM_REVIEW_TEXT = 5;

    public static final String FOLDER_ID = "1aVfkmsLg7lMD5Znk7Fh9669nGn21s_R9"; // 특정 폴더 ID
    public static final String USER_FILE_NAME = "users_v02.json";
    public static final String REVIEW_FILE_NAME = "reviews_v02.json";
    public static final String RATING_FILE_NAME = "rating_v01.json";
    public static final String FAVORITE_FILE_NAME = "favorite_v02.json";
    public static final String ITEM_FILE_PATH = "resources/data/kinolights_movie_details.json";

    public static final String UI_BACKGROUND_HEX = "#404153";
    public static final String UI_SUB_TEXT_HEX = "#A4A4A4";
    public static final String UI_MAIN_TEXT_HEX = "#CBCBCB";
    public static final String UI_TEXT_BOX_HEX = "#ECECEC";
    public static final String UI_POINT_COLOR_HEX = "#78DBA6";
}
