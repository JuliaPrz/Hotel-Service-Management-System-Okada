package application.encapsulatedData;

public class RoomType {
	
	private Integer rtID;
	private String rtName;
	private Double rtPrice;
	private String bedType;
	private String rtDescription;
	private String rtAmenities;
	private String rtImage;
	
	public RoomType (Integer rtID, String rtName, Double rtPrice, String bedType, String rtDescription, String rtAmenities, String rtImage) {
		
		this.rtID = rtID;
		this.rtName = rtName;
		this.rtPrice = rtPrice;
		this.bedType = bedType;
		this.rtDescription = rtDescription;
		this.rtAmenities = rtAmenities;
		this.rtImage = rtImage;
	}
	

	public Integer getRtID() {
		return rtID;
	}
	public void setRtID(Integer rtID) {
		this.rtID = rtID;
	}
	public String getRtName() {
		return rtName;
	}
	public void setRtName(String rtName) {
		this.rtName = rtName;
	}
	public Double getRtPrice() {
		return rtPrice;
	}
	public void setRtPrice(Double rtPrice) {
		this.rtPrice = rtPrice;
	}
	public String getBedType() {
		return bedType;
	}
	public void setBedType(String bedType) {
		this.bedType = bedType;
	}
	public String getRtDescription() {
		return rtDescription;
	}
	public void setRtDescription(String rtDescription) {
		this.rtDescription = rtDescription;
	}
	public String getRtAmenities() {
		return rtAmenities;
	}
	public void setRtAmenities(String rtAmenities) {
		this.rtAmenities = rtAmenities;
	}
	public String getRtImage() {
		return rtImage;
	}
	public void setRtImage(String rtImage) {
		this.rtImage = rtImage;
	}
	
	
}
