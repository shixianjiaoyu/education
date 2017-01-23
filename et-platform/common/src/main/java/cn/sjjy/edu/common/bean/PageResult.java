package cn.sjjy.edu.common.bean;

import java.util.Collections;
import java.util.List;

/** 
 * @author Captain
 * @date 2017年1月23日
 * @param <T>
 */
public class PageResult<T> {

    private int total;

    private List<T> records;

    public PageResult() {
    }

    public PageResult(int total, List<T> records) {
        this.total = total;
        this.records = records;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public final static PageResult EMPTY_PAGE_RESULT = new PageResult(0, Collections.emptyList());

    public static final class Builder<T> {
        private int total;
        private List<T> records;

        private Builder() {
        }

        public Builder<T> total(int total) {
            this.total = total;
            return this;
        }

        public Builder<T> records(List<T> records) {
            this.records = records;
            return this;
        }

        public PageResult<T> build() {
            PageResult<T> pageResult = new PageResult<T>();
            pageResult.setTotal(total);
            pageResult.setRecords(records);
            return pageResult;
        }
    }
}
