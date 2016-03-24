package qdh.pageModel;

/**
 * EasyUI 分页帮助类
 * 
 * @author 孙宇
 * 
 */
public class PageHelper implements java.io.Serializable {

	private int currentPage;// 当前页
	private int rowsPerPage;// 每页显示记录数
	private int totalRows; //总行数
	private int firstRow;
	
	public PageHelper(){
		
	}
	
	public PageHelper(Integer rowsPerPage, Integer currentPage,  Integer totalRows){
		this.currentPage = currentPage;
		this.rowsPerPage = rowsPerPage;
		this.totalRows = totalRows;
		firstRow = rowsPerPage * (currentPage -1);
	}
	
	/**
	 * array of first row , max records
	 * @return
	 */
	public Integer[] getPager(){
		Integer[] pager = new Integer[]{getFirstRow(), getRowPerPage()};
		return pager;
	}

    public int getFirstRow(){
    	return firstRow;
    }

    
    public int getTotalRows(){
    	return totalRows;
    }
    
    public int getRowPerPage(){
    	return rowsPerPage;
    }
}
