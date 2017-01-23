package cn.sjjy.edu.common.util;

/** 
 * @author Captain
 * @date 2017年1月23日
 */
public class Page {

    private final static int DEFAULT_OFFSET = 0;
    private final static int DEFAULT_LIMIT = 20;
    private int limit;
    private int offset;

    public Page(int offset, int limit) {
        this.limit = limit;
        this.offset = offset;
    }

    public Page() {
        this.limit = DEFAULT_LIMIT;
        this.offset = DEFAULT_OFFSET;
    }

    public static Page valueOf(Page page) {
        return (null == page) ? new Page() : page;
    }

    private static boolean isPageConditionPassing(int page, int pageSize) {
        return page >= 0 && pageSize >= 0;
    }

    public static Page valueOf(int page, int pageSize) {
        return isPageConditionPassing(page, pageSize) ? new Page((page - 1) * pageSize, pageSize) :
                new Page();
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "Page{" +
                "limit=" + limit +
                ", offset=" + offset +
                '}';
    }
}
