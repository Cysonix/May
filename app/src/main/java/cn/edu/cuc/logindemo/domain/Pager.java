package cn.edu.cuc.logindemo.domain;

/**
 * "分页"实体类,用于大数据量数据查询时存储分页信息。
 * @author SongQing
 *
 */
public class Pager {
	private int totalNum;			//总条数
	private int pageSize;			//每页条数
	private int currentPage;		//当前页
	private int startIndex;		//起始索引

	/**
	 * 构造函数
	 */
	public Pager(){

	}

	/**
	 * @return the totalNum
	 */
	public int getTotalNum() {
		return totalNum;
	}
	/**
	 * @param totalNum the totalNum to set
	 */
	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}
	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}
	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	/**
	 * @return the currentPage
	 */
	public int getCurrentPage() {
		return currentPage;
	}
	/**
	 * @param currentPage the currentPage to set
	 */
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getTotalPageCount(){
		int temp = this.totalNum % this.pageSize;

		if(temp == 0){
			int totalPage = this.totalNum / this.pageSize;
			return totalPage == 0 ? 1 : totalPage;
		}
		else {
			return this.totalNum / this.pageSize + 1;
		}
	}

	/**
	 * 获取普通新闻的默认分页设置
	 * @return 20
	 */
	public static Pager getDefault(){
		Pager pager = new Pager();
		pager.setCurrentPage(1);
		pager.setPageSize(getDefaultSize());
		pager.setStartIndex(0);

		return pager;
	}

	/**
	 * 获取焦点新闻分页设置
	 * @return 5
	 */
	public static Pager getTopDefault(){
		Pager pager = new Pager();
		pager.setCurrentPage(1);
		pager.setPageSize(5);
		pager.setStartIndex(0);

		return pager;
	}

	public static int getDefaultSize(){
		return 20;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getStartIndex() {
		return startIndex;
	}
}

