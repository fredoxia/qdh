package qdh.controller.formBean;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import qdh.utility.DateUtility;

/**
 * 更新 基础资料的bean
 * @author xiaf
 *
 */
public class ProductBasicFormBean {
	private String basicData;
	private Integer basicDataId;
	@DateTimeFormat(pattern="yyyy-mm-dd")
	private Date startDate = new Date(DateUtility.getToday().getTime());
	@DateTimeFormat(pattern="yyyy-mm-dd")
	private Date endDate = new Date(DateUtility.getToday().getTime());

	
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getBasicData() {
		return basicData;
	}

	public void setBasicData(String basicData) {
		this.basicData = basicData;
	}

	public Integer getBasicDataId() {
		return basicDataId;
	}

	public void setBasicDataId(Integer basicDataId) {
		this.basicDataId = basicDataId;
	}
	
	
}
