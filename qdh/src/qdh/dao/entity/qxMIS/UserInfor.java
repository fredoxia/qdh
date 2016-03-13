package qdh.dao.entity.qxMIS;

import java.io.Serializable;
import java.util.Date;

public class UserInfor implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5079784179894810789L;
	public static final String ACCOUNTANT_CODE = "01";
	public static final String SALES_CODE = "02";
	public static final String LOGISTIC_CODE = "03";
	public static final String ADMIN_CODE = "00";
	
	public static final String SWITCH_CHAIN_FUNCTION = "userJSP!swithToChain";

	public static final int SUPER_ADMIN = 99;
	
	public static final int NORMAL_ACCOUNT = 0;
	public static final int RESIGNED = 1;
	
	private int userId;
	private String userName;
	private String pinyin;
	private String name;
	private String password;
	private int roleType;
	private Date onBoardDate = new Date();
	private String department;
	private Date birthday = new Date();
	private String mobilePhone;
	private String homePhone;
	private String idNumber;
	private double baseSalary;
	private double baseVacation;
	private String jobTitle;
	private int jinsuanID;

	
	/**
	 * 0 means normal 
	 * 1 means resigned/retired, the account is disabled
	 */
	private int resign = 0;

	public int getJinsuanID() {
		return jinsuanID;
	}
	public void setJinsuanID(int jinsuanID) {
		this.jinsuanID = jinsuanID;
	}


	public Date getOnBoardDate() {
		return onBoardDate;
	}
	public void setOnBoardDate(Date onBoardDate) {
		this.onBoardDate = onBoardDate;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getHomePhone() {
		return homePhone;
	}
	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	public double getBaseSalary() {
		return baseSalary;
	}
	public void setBaseSalary(double baseSalary) {
		this.baseSalary = baseSalary;
	}
	public double getBaseVacation() {
		return baseVacation;
	}
	public void setBaseVacation(double baseVacation) {
		this.baseVacation = baseVacation;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int user_id) {
		this.userId = user_id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String user_name) {
		this.userName = user_name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getRoleType() {
		return roleType;
	}
	public void setRoleType(int roleType) {
		this.roleType = roleType;
	}
	public int getResign() {
		return resign;
	}
	public void setResign(int resign) {
		this.resign = resign;
	}

	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + userId;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserInfor other = (UserInfor) obj;
		if (userId != other.userId)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "UserInfor [user_id=" + userId + ", user_name=" + userName
				+ ", name=" + name + ", password=" + password + ", roleType="
				+ roleType + ", onBoardDate=" + onBoardDate + ", department="
				+ department + ", birthday=" + birthday + ", mobilePhone="
				+ mobilePhone + ", homePhone=" + homePhone + ", idNumber="
				+ idNumber + ", baseSalary=" + baseSalary + ", baseVacation="
				+ baseVacation + ", jobTitle=" + jobTitle + ", jinsuanID="
				+ jinsuanID + ", resign=" + resign + ", functions=" 
				+ "]";
	}

}
