package entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by intellij IDEA
 *
 * @author Raven
 * Date:2019/1/7
 * Time:15:24
 * 分页结果类
 */
public class PageResult implements Serializable {
    //总记录数
    private long total;
    //当前页记录
    private List rows;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }

    public PageResult(long total, List rows) {

        this.total = total;
        this.rows = rows;
    }
}
