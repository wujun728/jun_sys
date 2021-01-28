package com.doroodo.sys.model;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.doroodo.base.model.BaseModel;
import com.doroodo.code.util.DateConvertUtils;
public class SySbProjectImagesInfos_Vo  extends BaseModel implements java.io.Serializable{
	

	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	private String id;
	private String ptimitiveId;
	private String imageUname;
	private String imgName;
	private String updateTime;
	//columns END


	public void setId(String value) {
		this.id = value;
	}
	
	public String getImageUname() {
		return imageUname;
	}

	public void setImageUname(String imageUname) {
		this.imageUname = imageUname;
	}

		public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

		public String getId() {
		return this.id;
	}
	
	public String getPtimitiveId() {
		return this.ptimitiveId;
	}
	
	public void setPtimitiveId(String value) {
		this.ptimitiveId = value;
	}
	
	public String getUpdateTime() {
		return this.updateTime;
	}
	
	public void setUpdateTime(String value) {
		this.updateTime = value;
	}
	

	public String toString() {
		return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
			.append("Id",getId())
			.append("PtimitiveId",getPtimitiveId())
			.append("imageUname",getImageUname())
			.append("imgName",getImgName())
			.append("UpdateTime",getUpdateTime())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getId())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof SySbProjectImagesInfos == false) return false;
		if(this == obj) return true;
		SySbProjectImagesInfos other = (SySbProjectImagesInfos)obj;
		return new EqualsBuilder()
			.append(getId(),other.getId())
			.isEquals();
	}
}

