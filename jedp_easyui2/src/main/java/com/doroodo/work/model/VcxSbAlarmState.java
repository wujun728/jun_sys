package com.doroodo.work.model;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.doroodo.base.model.BaseModel;
import com.doroodo.code.util.DateConvertUtils;
@Entity
@Table(name = "v_cx_sb_alarm_state")
public class VcxSbAlarmState  extends BaseModel implements java.io.Serializable{
	public  final String FORMAT_SAVE_DATE = DATE_FORMAT;
	

	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	private java.lang.Integer id;
	@Length(max=20)
	private java.lang.String alarmState;
	@Length(max=20)
	private java.lang.String brakeState;
	@Length(max=20)
	private java.lang.String cause;
	@Length(max=32)
	private java.lang.String terminalId;
	
	private java.util.Date saveDate;
	@Length(max=32)
	private java.lang.String alarmDesc;
	@Length(max=32)
	private java.lang.String brakeDesc;
	@Length(max=32)
	private java.lang.String causeDesc;
	@Length(max=20)
	private java.lang.String line;
	@Length(max=20)
	private java.lang.String factory;
	@Length(max=32)
	private java.lang.String protocol;
	@Length(max=50)
	private java.lang.String position;
	@Length(max=32)
	private java.lang.String protocolName;
	@Length(max=255)
	private java.lang.String simCard;
	//columns END


	public VcxSbAlarmState(){
	}

	@Id @GeneratedValue(generator="custom-id")
	@GenericGenerator(name="custom-id", strategy = "increment")
	@Column(name = "id", unique = false, nullable = false, insertable = true, updatable = true, length = 10)
	public java.lang.Integer getId() {
		return this.id;
	}
	
	public void setId(java.lang.Integer value) {
		this.id = value;
	}
	
	@Column(name = "alarm_state", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public java.lang.String getAlarmState() {
		return this.alarmState;
	}
	
	public void setAlarmState(java.lang.String value) {
		this.alarmState = value;
	}
	
	@Column(name = "brake_state", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public java.lang.String getBrakeState() {
		return this.brakeState;
	}
	
	public void setBrakeState(java.lang.String value) {
		this.brakeState = value;
	}
	
	@Column(name = "cause", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public java.lang.String getCause() {
		return this.cause;
	}
	
	public void setCause(java.lang.String value) {
		this.cause = value;
	}
	
	@Column(name = "terminal_id", unique = false, nullable = true, insertable = true, updatable = true, length = 32)
	public java.lang.String getTerminalId() {
		return this.terminalId;
	}
	
	public void setTerminalId(java.lang.String value) {
		this.terminalId = value;
	}
	
	@Transient
	public String getSaveDateString() {
		return DateConvertUtils.format(getSaveDate(), FORMAT_SAVE_DATE);
	}
	public void setSaveDateString(String value) {
		setSaveDate(DateConvertUtils.parse(value, FORMAT_SAVE_DATE,java.util.Date.class));
	}
	
	@Column(name = "save_date", unique = false, nullable = true, insertable = true, updatable = true, length = 19)
	public java.util.Date getSaveDate() {
		return this.saveDate;
	}
	
	public void setSaveDate(java.util.Date value) {
		this.saveDate = value;
	}
	
	@Column(name = "alarm_desc", unique = false, nullable = true, insertable = true, updatable = true, length = 32)
	public java.lang.String getAlarmDesc() {
		return this.alarmDesc;
	}
	
	public void setAlarmDesc(java.lang.String value) {
		this.alarmDesc = value;
	}
	
	@Column(name = "brake_desc", unique = false, nullable = true, insertable = true, updatable = true, length = 32)
	public java.lang.String getBrakeDesc() {
		return this.brakeDesc;
	}
	
	public void setBrakeDesc(java.lang.String value) {
		this.brakeDesc = value;
	}
	
	@Column(name = "cause_desc", unique = false, nullable = true, insertable = true, updatable = true, length = 32)
	public java.lang.String getCauseDesc() {
		return this.causeDesc;
	}
	
	public void setCauseDesc(java.lang.String value) {
		this.causeDesc = value;
	}
	
	@Column(name = "line", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public java.lang.String getLine() {
		return this.line;
	}
	
	public void setLine(java.lang.String value) {
		this.line = value;
	}
	
	@Column(name = "factory", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public java.lang.String getFactory() {
		return this.factory;
	}
	
	public void setFactory(java.lang.String value) {
		this.factory = value;
	}
	
	@Column(name = "protocol", unique = false, nullable = true, insertable = true, updatable = true, length = 32)
	public java.lang.String getProtocol() {
		return this.protocol;
	}
	
	public void setProtocol(java.lang.String value) {
		this.protocol = value;
	}
	
	@Column(name = "position", unique = false, nullable = true, insertable = true, updatable = true, length = 50)
	public java.lang.String getPosition() {
		return this.position;
	}
	
	public void setPosition(java.lang.String value) {
		this.position = value;
	}
	
	@Column(name = "protocol_name", unique = false, nullable = true, insertable = true, updatable = true, length = 32)
	public java.lang.String getProtocolName() {
		return this.protocolName;
	}
	
	public void setProtocolName(java.lang.String value) {
		this.protocolName = value;
	}
	
	@Column(name = "sim_card", unique = false, nullable = true, insertable = true, updatable = true, length = 255)
	public java.lang.String getSimCard() {
		return this.simCard;
	}
	
	public void setSimCard(java.lang.String value) {
		this.simCard = value;
	}
	

	public String toString() {
		return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
			.append("Id",getId())
			.append("AlarmState",getAlarmState())
			.append("BrakeState",getBrakeState())
			.append("Cause",getCause())
			.append("TerminalId",getTerminalId())
			.append("SaveDate",getSaveDate())
			.append("AlarmDesc",getAlarmDesc())
			.append("BrakeDesc",getBrakeDesc())
			.append("CauseDesc",getCauseDesc())
			.append("Line",getLine())
			.append("Factory",getFactory())
			.append("Protocol",getProtocol())
			.append("Position",getPosition())
			.append("ProtocolName",getProtocolName())
			.append("SimCard",getSimCard())
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof VcxSbAlarmState == false) return false;
		if(this == obj) return true;
		VcxSbAlarmState other = (VcxSbAlarmState)obj;
		return new EqualsBuilder()
			.isEquals();
	}
}

