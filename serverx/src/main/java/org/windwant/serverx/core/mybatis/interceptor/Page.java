package org.windwant.serverx.core.mybatis.interceptor;

import org.windwant.serverx.Constants;

/**
 * Created by Administrator on 2018/1/5.
 */
public class Page {

    private int offset = 0;

    private int limit = Constants.DEAFULT_PAGE_LIMIT;

    private int total = 0;

    private int page = 1;

    private boolean count = true;

    public boolean isCount() {
        return count;
    }

    public void setCount(boolean count) {
        this.count = count;
    }

    public Page(){}

    public Page(int page){
        this.page = page;
    }

    public Page(int offset, int limit){
        this. offset = offset;
        this.limit = limit;
    }
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page < 1?1:page;
    }

    public int getOffset() {
        return (page - 1) * limit;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
