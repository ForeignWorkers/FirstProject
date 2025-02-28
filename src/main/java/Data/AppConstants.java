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
}
