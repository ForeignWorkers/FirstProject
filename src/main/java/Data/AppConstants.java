package Data;

public final class AppConstants {
    // private 생성자로 인스턴스화 방지
    private AppConstants() {
        throw new UnsupportedOperationException("상수는 private 만들수 없습니다.");
    }

    public static final int FRAME_HEIGHT = 1133;
    public static final int FRAME_WIDTH = 917;
}
