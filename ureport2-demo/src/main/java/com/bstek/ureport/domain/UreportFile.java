package com.bstek.ureport.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Date;

@Entity
@Table(name = "ureport_file", schema = "h_cloud", catalog = "")
public class UreportFile {
	private long id;
	private String name;
	private String title;
	private transient  byte[] content;
	private Date createTime;
	private Date updateTime;
	private String userId;
	private Byte status;
	private Byte pubLevel;

	@Id
	@Column(name = "id", nullable = false)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "name", nullable = true, length = 255)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Basic
	@Column(name = "title", nullable = true, length = 255)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Basic
	@Column(name = "content", nullable = true)
	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}


	@Basic
	@Column(name = "create_time")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat( pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Basic
	@Column(name = "update_time")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat( pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Basic
	@Column(name = "user_id", nullable = true, length = 12)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Basic
	@Column(name = "status", nullable = true)
	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	@Basic
	@Column(name = "pub_level", nullable = true)
	public Byte getPubLevel() {
		return pubLevel;
	}

	public void setPubLevel(Byte pubLevel) {
		this.pubLevel = pubLevel;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		UreportFile that = (UreportFile) o;

		if (id != that.id) return false;
		if (name != null ? !name.equals(that.name) : that.name != null) return false;
		if (title != null ? !title.equals(that.title) : that.title != null) return false;
		if (!Arrays.equals(content, that.content)) return false;
		if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
		if (updateTime != null ? !updateTime.equals(that.updateTime) : that.updateTime != null) return false;
		if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
		if (status != null ? !status.equals(that.status) : that.status != null) return false;
		if (pubLevel != null ? !pubLevel.equals(that.pubLevel) : that.pubLevel != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = (int) (id ^ (id >>> 32));
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (title != null ? title.hashCode() : 0);
		result = 31 * result + Arrays.hashCode(content);
		result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
		result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
		result = 31 * result + (userId != null ? userId.hashCode() : 0);
		result = 31 * result + (status != null ? status.hashCode() : 0);
		result = 31 * result + (pubLevel != null ? pubLevel.hashCode() : 0);
		return result;
	}
}
