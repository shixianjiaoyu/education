package cn.sjjy.edu.web.framework.core;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/** 
 * @author Captain
 * @date 2017年1月23日
 * @param <E>
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Pagination<E> {
    private Integer page;
    private Integer total_count;
    private E data;

    public Pagination(Integer page, Integer total_count, E data) {
        this.page = page;
        this.total_count = total_count;
        this.data = data;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotal_count() {
        return total_count;
    }

    public void setTotal_count(Integer total_count) {
        this.total_count = total_count;
    }

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }

}
