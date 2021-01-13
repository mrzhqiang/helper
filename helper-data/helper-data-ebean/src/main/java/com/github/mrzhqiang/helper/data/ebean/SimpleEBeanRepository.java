package com.github.mrzhqiang.helper.data.ebean;

import com.github.mrzhqiang.helper.data.domain.Page;
import com.github.mrzhqiang.helper.data.domain.Pageable;
import com.github.mrzhqiang.helper.data.domain.Paging;
import com.github.mrzhqiang.helper.data.util.Datas;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import io.ebean.ExpressionList;
import io.ebean.Finder;
import io.ebean.PagedList;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * 基于 EBean 的实体仓库默认实现。
 *
 * @author mrzhqiang
 */
@Slf4j(topic = "helper.data.ebean")
public class SimpleEBeanRepository<T, ID> implements EBeanRepository<T, ID> {

    private final Finder<ID, T> finder;

    public SimpleEBeanRepository(Class<T> type) {
        this.finder = new Finder<>(type);
    }

    public SimpleEBeanRepository(Class<T> type, String serverName) {
        this.finder = new Finder<>(type, serverName);
    }

    @Override
    public T save(T entity) {
        Datas.accept(entity, finder.db()::save);
        return entity;
    }

    @Override
    public Optional<T> findById(ID id) {
        T entity = Datas.apply(id, finder::byId);
        return Optional.ofNullable(entity);
    }

    @Override
    public long count() {
        return Datas.get(() -> finder.query().findCount());
    }

    @Override
    public void deleteById(ID id) {
        Datas.accept(id, finder::deleteById);
    }

    @Override
    public void delete(T entity) {
        Datas.accept(entity, finder.db()::delete);
    }

    @Override
    public void deleteAll(Iterable<T> entities) {
        Preconditions.checkArgument(Objects.nonNull(entities), "entities == null");

        List<T> datas = Lists.newArrayList(entities);
        Datas.apply(datas, finder.db()::deleteAll);
    }

    @Override
    public void deleteAll() {
        // fixme 需要软删除生效，否则方法不允许调用
        Datas.get(() -> finder.query().delete());
    }

    @Override
    public List<T> findAll() {
        return Datas.get(finder::all);
    }

    @Override
    public List<T> saveAll(Iterable<T> entities) {
        Preconditions.checkArgument(Objects.nonNull(entities), "entities == null");

        List<T> datas = Lists.newArrayList(entities);
        Datas.apply(datas, finder.db()::saveAll);
        return datas;
    }

    @Override
    public Page<T> findAll(Pageable pageable, @Nullable Map<String, Object> where) {
        Preconditions.checkArgument(Objects.nonNull(pageable), "pageable == null");

        ExpressionList<T> clauses = EBeanRepository.ofClauses(finder.query().where(), where);

        if (pageable.isUnpaged()) {
            return Paging.of(Datas.get(clauses::findList));
        }

        clauses.setFirstRow((int) pageable.getOffset());
        clauses.setMaxRows(pageable.getPageSize());

        PagedList<T> pagedList = Datas.apply(clauses, ExpressionList::findPagedList);
        if (Objects.nonNull(pagedList)) {
            pagedList.loadCount();
            List<T> content = Datas.get(pagedList::getList);
            return Paging.of(content, pageable, pagedList.getTotalCount());
        }

        return Page.empty(pageable);
    }
}
