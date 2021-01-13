package com.github.mrzhqiang.helper.data.cassandra;

import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.core.querybuilder.Truncate;
import com.datastax.driver.mapping.Mapper;
import com.github.mrzhqiang.helper.data.DataAccessException;
import com.github.mrzhqiang.helper.data.domain.PageRequest;
import com.github.mrzhqiang.helper.data.domain.Pageable;
import com.github.mrzhqiang.helper.data.domain.Slice;
import com.github.mrzhqiang.helper.data.domain.Slicing;
import com.github.mrzhqiang.helper.data.util.Datas;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.*;

/**
 * 基于 Cassandra 的抽象仓库。
 * <p>
 * 简化 CQL 语句的相关操作。
 *
 * @author mrzhqiang
 */
public class SimpleCassandraRepository<T> implements CassandraRepository<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger("helper.data.cassandra");

    /**
     * Cassandra 实体映射器。
     */
    private final Mapper<T> mapper;

    public SimpleCassandraRepository(Mapper<T> mapper) {
        Preconditions.checkNotNull(mapper, "mapper == null");
        this.mapper = mapper;
    }

    @Nullable
    @Override
    public T save(T entity) {
        Preconditions.checkArgument(Objects.nonNull(entity), "entity == null");

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Save entity: {}", entity);
        }

        Session session = mapper.getManager().getSession();
        Statement statement = Datas.apply(entity, mapper::saveQuery);
        ResultSet resultSet = Datas.apply(statement, session::execute);
        return Objects.isNull(resultSet) ? null : mapper.map(resultSet).one();
    }

    @Override
    public final Optional<T> findById(Object... ids) {
        Preconditions.checkArgument(Objects.nonNull(ids), "ids == null");
        Preconditions.checkArgument(ids.length > 0, "ids is empty");

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Find by primary key: {}", Arrays.toString(ids));
        }

        return Optional.ofNullable(Datas.apply(ids, mapper::get));
    }

    @Override
    public List<T> findAllById(Iterable<Object> mapIds) {
        FindByIdQuery mapIdQuery = FindByIdQuery.forIds(mapIds);
        List<Object> idCollection = mapIdQuery.getIdCollection();
        String idField = mapIdQuery.getIdProperty();

        if (idCollection.isEmpty()) {
            return Collections.emptyList();
        }

        if (Objects.isNull(idField)) {
            throw new IllegalArgumentException("find all by id must be has id filed name.");
        }

        TableMetadata tableMetadata = mapper.getTableMetadata();
        Session session = mapper.getManager().getSession();
        Select.Where whereCQL = QueryBuilder.select().from(tableMetadata)
                .where(QueryBuilder.in(idField, idCollection));
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Execute find all cql: {}", whereCQL);
        }

        ResultSet resultSet = Datas.apply(whereCQL, session::execute);
        if (Objects.nonNull(resultSet) && resultSet.getAvailableWithoutFetching() > 0) {
            List<T> all = mapper.map(resultSet).all();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("tableMetadata {} find all by id size {}", tableMetadata, all.size());
            }
            return all;
        }

        return Collections.emptyList();
    }

    @Override
    public boolean existsById(Object mapId) {
        return findById(mapId).isPresent();
    }

    @Override
    public List<T> findAll(@Nullable Map<String, Object> where) {
        TableMetadata tableMetadata = mapper.getTableMetadata();
        Select.Where whereCQL = QueryBuilder.select().all().from(tableMetadata).where();
        CassandraRepository.ofClauses(where).forEach(whereCQL::and);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Execute find all cql: {}", whereCQL);
        }

        Session session = mapper.getManager().getSession();
        ResultSet resultSet = Datas.apply(whereCQL, session::execute);
        if (Objects.nonNull(resultSet) && resultSet.getAvailableWithoutFetching() > 0) {
            List<T> all = mapper.map(resultSet).all();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("tableMetadata {} find all size {}", tableMetadata, all.size());
            }
            return all;
        }

        return Collections.emptyList();
    }

    @Override
    public Optional<T> findById(Object mapId) {
        Preconditions.checkArgument(Objects.nonNull(mapId), "map id == null");

        if (mapId instanceof MapId) {
            MapId id = (MapId) mapId;
            Iterator<String> iterator = id.keySet().iterator();

            if (id.size() > 1) {
                throw new DataAccessException("MapId with multiple keys are not supported");
            }

            if (!iterator.hasNext()) {
                throw new DataAccessException("MapId is empty");
            }

            String idField = iterator.next();
            Object idValue = id.get(idField);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Find by id filed:{}, id value:{}", idField, idValue);
            }

            // 不拼接 字段:查询类型 的情况下，默认使用 eq 进行等值比较
            List<T> all = findAll(Collections.singletonMap(idField, idValue));
            return all.isEmpty() ? Optional.empty() : Optional.of(all.get(0));
        }

        int pkSize = mapper.getTableMetadata().getPrimaryKey().size();
        if (pkSize == 1) {
            // 主键数量为 1 的情况下，可以借用 findById 返回的值
            return findById(new Object[]{mapId});
        }

        // 不满足条件的情况下，无法检测到有效数据，所以只能返回不存在
        return Optional.empty();
    }

    /**
     * 通过分页信息和查询子句，获得分页数据。
     *
     * @param pageable 分页信息。
     * @param where    查询子句。
     * @return 资源列表。如果没有查询到，则返回 Null。
     */
    @Override
    public Slice<T> findAll(Pageable pageable, @Nullable Map<String, Object> where) {
        Preconditions.checkArgument(Objects.nonNull(pageable), "pageable == null");

        if (pageable.isUnpaged()) {
            List<T> results = Objects.isNull(where) ? findAll() : findAll(where);
            return Slicing.of(results);
        }

        CassandraPageRequest.validatePageable(pageable);

        Session session = mapper.getManager().getSession();
        Select.Where whereCQL = QueryBuilder.select().all().from(mapper.getTableMetadata()).where();
        CassandraRepository.ofClauses(where).forEach(whereCQL::and);
        int pageSize = pageable.getPageSize();
        whereCQL.setFetchSize(pageSize);

        if (pageable instanceof CassandraPageRequest) {
            whereCQL.setPagingState(((CassandraPageRequest) pageable).getPagingState());
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Execute page cql: {}", where);
        }

        ResultSet resultSet = session.execute(whereCQL);
        PagingState pagingState = resultSet.getExecutionInfo().getPagingState();
        // 有 pagingState 基本上不需要设置 page （页面编号），这里参考 Spring Boot Data 设置为 0
        CassandraPageRequest pageRequest = CassandraPageRequest.of(
                PageRequest.of(0, pageSize), pagingState);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("generate: {}", pageRequest);
        }

        List<T> content = mapper.map(resultSet).all();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("slice find all size: {}, page size: {}", content.size(), pageSize);
        }

        return Slicing.of(content, pageRequest, Objects.nonNull(pagingState));
    }

    @Override
    public boolean existsById(Object... ids) {
        return findById(ids).isPresent();
    }

    /**
     * 根据实体映射器以及查询子句，获得资源总数量。
     *
     * @param where 查询子句。允许为 Null，表示。
     * @return 0 表示没有任何资源，大于 0 表示查询到的资源总数量。
     */
    @Override
    public long countBy(@Nullable Map<String, Object> where) {
        TableMetadata tableMetadata = mapper.getTableMetadata();
        Select.Where whereCQL = QueryBuilder.select().countAll().from(tableMetadata).where();
        CassandraRepository.ofClauses(where).forEach(whereCQL::and);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Execute count cql: {}", whereCQL);
        }

        Session session = mapper.getManager().getSession();
        ResultSet countSet = Datas.apply(whereCQL, session::execute);
        long total = 0L;
        if (Objects.nonNull(countSet) && countSet.getAvailableWithoutFetching() > 0) {
            total = countSet.one().getLong("count");
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("tableMetadata {} count all {}", tableMetadata, total);
        }

        return total;
    }

    @Override
    public void deleteById(Object mapId) {
        findById(mapId).ifPresent(this::delete);
    }

    @Override
    public final void deleteById(Object... ids) {
        Preconditions.checkArgument(Objects.nonNull(ids), "ids == null");
        Preconditions.checkArgument(ids.length > 0, "ids is empty");

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Delete primary key of arrays: {}", Arrays.toString(ids));
        }

        Datas.accept(ids, mapper::delete);
    }

    @Override
    public void delete(T entity) {
        Preconditions.checkArgument(Objects.nonNull(entity), "entity == null");
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Delete entity: {}", entity);
        }

        Datas.accept(entity, mapper::delete);
    }

    @Override
    public void deleteAll() {
        Session session = mapper.getManager().getSession();
        TableMetadata tableMetadata = mapper.getTableMetadata();
        Truncate truncate = QueryBuilder.truncate(tableMetadata);
        ResultSet resultSet = Datas.apply(truncate, session::execute);
        if (Objects.nonNull(resultSet) && LOGGER.isDebugEnabled()) {
            LOGGER.debug("tableMetadata {} delete all size {}", tableMetadata, resultSet.getAvailableWithoutFetching());
        }
    }
}
