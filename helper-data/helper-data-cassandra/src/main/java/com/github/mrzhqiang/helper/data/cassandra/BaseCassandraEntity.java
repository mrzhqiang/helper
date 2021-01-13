package com.github.mrzhqiang.helper.data.cassandra;

import com.datastax.driver.core.DataType;
import com.datastax.driver.core.schemabuilder.Create;
import com.datastax.driver.core.schemabuilder.SchemaBuilder;
import com.datastax.driver.extras.codecs.jdk8.InstantCodec;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.annotations.Column;
import lombok.Data;

import java.time.Instant;

/**
 * Cassandra 实体。
 * <p>
 * CQL 数据库除了主键之外，还有分区键，为了保证 {@link Mapper} 的正常使用，实体将只使用分区键。
 *
 * @author mrzhqiang
 */
@Data
public abstract class BaseCassandraEntity {

    protected static final String COL_COMMON_CREATED = "created";
    protected static final String COL_COMMON_MODIFIED = "modified";
    protected static final String COL_COMMON_DESCRIPTION = "description";

    @Column(name = COL_COMMON_CREATED, codec = InstantCodec.class)
    protected Instant created = Instant.now();
    @Column(name = COL_COMMON_MODIFIED, codec = InstantCodec.class)
    protected Instant modified = Instant.now();
    @Column(name = COL_COMMON_DESCRIPTION)
    protected String description;

    /**
     * 返回秘钥空间名字。
     *
     * @return 秘钥空间名字。
     */
    public abstract String keyspaceName();

    /**
     * 返回表名。
     *
     * @return 表名。
     */
    public abstract String tableName();

    /**
     * 返回创建语句，用来检查表是否已创建。
     *
     * @return CQL 语句类。
     */
    public Create createCQL() {
        return SchemaBuilder.createTable(keyspaceName(), tableName())
                .ifNotExists()
                .addColumn(COL_COMMON_CREATED, DataType.timestamp())
                .addColumn(COL_COMMON_MODIFIED, DataType.timestamp())
                .addColumn(COL_COMMON_DESCRIPTION, DataType.text());
    }
}
