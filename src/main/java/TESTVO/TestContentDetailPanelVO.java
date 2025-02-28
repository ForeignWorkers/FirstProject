package TESTVO;

public class TestContentDetailPanelVO {
	
	private int itemID; //작품번호
    private String title; //제목
    private String genre; //장르
    private int promotionYear; //개봉년도
    private int promotionDay; //개봉일
    private int RunningTime; //러닝타임
    private String nation; //국가
    private String director; //감독
    private String actor; //배우
    private double rating; //등급
    private int personNum; //평점 등록자 수
    private double reviewTotal; // 리뷰 평점 누적 합
    
       //생성자
	public TestContentDetailPanelVO(int id, String title, String genre, int promotionYear, double rating, int personNum, double reviewTotal) {
		super();
		this.itemID = id;
		this.title = title;
		this.genre = genre;
		this.promotionYear = promotionYear;
		this.rating = rating;
		this.personNum = personNum;
		this.reviewTotal = reviewTotal;
	}
	
	public int getId() {
		return this.itemID;
	}

	// 리뷰 평점 산출하는 메서드 ( 리뷰 점수 총합 / 리뷰어 수 ) // 성근님 로컬VO참고
    public void avgReview() { 
        this.reviewTotal /= this.personNum;
        System.out.printf("리뷰 평점 : %.1f\n", this.reviewTotal);
    }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public int getPromotionYear() {
		return promotionYear;
	}

	public void setPromotionYear(int promotionYear) {
		this.promotionYear = promotionYear;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public int getPersonNum() {
		return personNum;
	}

	public void setPersonNum(int personNum) {
		this.personNum = personNum;
	}

	public double getReviewTotal() {
		return reviewTotal;
	}

	public void setReviewTotal(double reviewTotal) {
		this.reviewTotal = reviewTotal;
	}

	@Override
	public String toString() {
		return "TestVO [title=" + title + ", genre=" + genre + ", promotionYear=" + promotionYear + ", rating=" + rating
				+ ", personNum=" + personNum + ", reviewTotal=" + reviewTotal + "]";
	}
	
}//class