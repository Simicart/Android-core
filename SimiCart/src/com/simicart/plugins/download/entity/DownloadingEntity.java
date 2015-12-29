package com.simicart.plugins.download.entity;

public class DownloadingEntity {
	private String downloadingFileName;
	private String downloadingFileSize;
	private String downloadingDownloaded;
	private String downloadingSpeed;
	private String downloadingTimeLeft;
	private String downloadingID;
	
	public DownloadingEntity(String downloadingID) {
		super();
		this.downloadingID = downloadingID;
	}

	public String getDownloadingID() {
		return downloadingID;
	}

	public void setDownloadingID(String downloadingID) {
		this.downloadingID = downloadingID;
	}

	public String getDownloadingFileName() {
		return downloadingFileName;
	}
	public void setDownloadingFileName(String downloadingFileName) {
		this.downloadingFileName = downloadingFileName;
	}
	public String getDownloadingFileSize() {
		return downloadingFileSize;
	}
	public void setDownloadingFileSize(String downloadingFileSize) {
		this.downloadingFileSize = downloadingFileSize;
	}
	public String getDownloadingDownloaded() {
		return downloadingDownloaded;
	}
	public void setDownloadingDownloaded(String downloadingDownloaded) {
		this.downloadingDownloaded = downloadingDownloaded;
	}
	public String getDownloadingSpeed() {
		return downloadingSpeed;
	}
	public void setDownloadingSpeed(String downloadingSpeed) {
		this.downloadingSpeed = downloadingSpeed;
	}
	public String getDownloadingTimeLeft() {
		return downloadingTimeLeft;
	}
	public void setDownloadingTimeLeft(String downloadingTimeLeft) {
		this.downloadingTimeLeft = downloadingTimeLeft;
	}
	
	
}
