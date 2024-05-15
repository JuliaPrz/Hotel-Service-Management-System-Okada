package application.tableData;

public class Room {

	private Integer roomNum;
	private String rtName;
	private String rtPrice;
	private String roomStatus;
	
	public Room (Integer roomNum, String rtName, String rtPrice, String roomStatus) {
		
		this.roomNum = roomNum;
		this.rtName = rtName;
		this.rtPrice = rtPrice;
		this.roomStatus = roomStatus;
	}
	
	public Integer getRoomNum() {
		return roomNum;
	}

	public void setRoomNum(Integer roomNum) {
		this.roomNum = roomNum;
	}

	public String getRtName() {
		return rtName;
	}

	public void setRtName(String rtName) {
		this.rtName = rtName;
	}

	public String getRtPrice() {
		return rtPrice;
	}

	public void setRtPrice(String rtPrice) {
		this.rtPrice = rtPrice;
	}

	public String getRoomStatus() {
		return roomStatus;
	}

	public void setRoomStatus(String roomStatus) {
		this.roomStatus = roomStatus;
	}


	

	
	
}
