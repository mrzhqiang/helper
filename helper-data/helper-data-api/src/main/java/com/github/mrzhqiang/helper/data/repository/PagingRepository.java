package com.github.mrzhqiang.helper.data.repository;

import com.github.mrzhqiang.helper.data.domain.Page;
import com.github.mrzhqiang.helper.data.domain.Pageable;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Map;

/**
 * 提供分页功能的存储库。
 * <p>
 * 扩展于 增删改查存储库。
 *
 * @author mrzhqiang
 */
public interface PagingRepository<T, ID> extends CrudRepository<T, ID> {

    default Page<T> findAll(Pageable pageable) {
        return findAll(pageable, Collections.emptyMap());
    }

    /**
     * 获取指定参数的分页。
     *
     * @param pageable 分页信息。
     * @param where    where 查询子句。允许为 Null。
     * @return 分页数据。
     * @throws IllegalArgumentException 如果参数为 Null 或包含 Null 元素，则抛出非法参数异常。
     */
    Page<T> findAll(Pageable pageable, @Nullable Map<String, Object> where);
}
