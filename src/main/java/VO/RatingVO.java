package VO;

public class RatingVO {
	private int ItemId;
	private double ratingPoint;
	
	public RatingVO(int itemId, double ratingPoint) {
		ItemId = itemId;
		this.ratingPoint = ratingPoint;
	}

	public int getItemId() {
		return ItemId;
	}

	public void setItemId(int itemId) {
		ItemId = itemId;
	}

	public double getRatingPoint() {
		return ratingPoint;
	}

	public void setRatingPoint(double ratingPoint) {
		this.ratingPoint = ratingPoint;
	}
}
