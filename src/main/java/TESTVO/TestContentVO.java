package TESTVO;

public class TestContentVO {
	private String title; // 제목 - 썸네일 불러오기
	private double rating; // 리뷰 평점 - 매서드 활용해서 사용
	private boolean isFavorite; // 찜 여부
	private String thumbnailFile;// 썸네일 이미지 경로
	private String genres; // 장르

	// 데이터 받을 생성자
	public TestContentVO(String title, String genres, double rating, boolean isFavorite, String thumbnailFile) {
		this.title = title;
		this.rating = rating;
		this.isFavorite = isFavorite;
		this.thumbnailFile = thumbnailFile;
		this.genres = genres;
	}

	// geter seter 생성
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public boolean isFavorite() {
		return isFavorite;
	}

	public void setFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
	}

	public String getThumbnailFile() {
		return thumbnailFile;
	}

	public void setThumbnailFile(String thumbnailFile) {
		this.thumbnailFile = thumbnailFile;
	}

	public String getGenres() {
		return genres;
	}

	public void setGenres(String genres) {
		this.genres = genres;
	}
}
