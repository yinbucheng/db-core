package cn.intellif.db.core.base;

import java.io.Serializable;
import java.util.List;

public interface BaseDao<T> {
    /**
     * 保存数据,如果为自动增长id返回id
     * @param bean
     * @return
     */
     int save(T bean);

    /**
     * 批量插入数据
     * @param beans
     */
     int[] batchSave(List<T> beans);

    /**
     * 更新数据
     * @param bean
     * @return
     */
     int update(T bean);

    /**
     * 更新非空数据
     * @param bean
     * @return
     */
     int updateNotNull(T bean);

    /**
     * 根据主键
     * @param id
     * @return
     */
     T findOne(Serializable id);

    /**
     * 通过id删除制定数据
     * @param id
     * @return
     */
     int delete(Serializable id);



}
