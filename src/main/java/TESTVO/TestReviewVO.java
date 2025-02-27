package TESTVO;

public class TestReviewVO {
    private double reviewScore;  // 리뷰 점수
    private double reviewTotal;  // 리뷰 점수 총합
    private int iineCount;       // 좋아요 수
    private int personNum;       // 평점 등록자 수
    private String reviewContent; // 리뷰 내용
    private String reviewName;   // 리뷰 작성자명

    // 생성자
    public TestReviewVO(String reviewName, String reviewContent, double reviewScore) {
        this.reviewName = reviewName;
        this.reviewContent = reviewContent;
        this.reviewScore = reviewScore;
        this.reviewTotal = reviewScore; // 첫 리뷰 점수 그대로 저장
        this.personNum = 1; // 첫 리뷰어 1명
        this.iineCount = 0; // 좋아요 0개로 초기화
    }

    // getter & setter
    public double getReviewScore() {
        return reviewScore;
    }

    public void setReviewScore(double reviewScore) {
        this.reviewScore = reviewScore;
    }

    public double getReviewTotal() {
        return reviewTotal;
    }

    public void addReviewScore(double score) {
        this.reviewTotal += score;
        this.personNum++; // 리뷰어 수 증가
    }

    public int getIineCount() {
        return iineCount;
    }

    public void addLike() {
        this.iineCount++;
    }

    public void removeLike() {
        if (this.iineCount > 0) {
            this.iineCount--;
        }
    }

    public int getPersonNum() {
        return personNum;
    }

    public double getAverageScore() {
        return personNum > 0 ? reviewTotal / personNum : 0;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public String getReviewName() {
        return reviewName;
    }

    public void setReviewName(String reviewName) {
        this.reviewName = reviewName;
    }
}
