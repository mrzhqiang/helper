package com.github.mrzhqiang.helper.data.ebean;

import io.ebean.Model;
import io.ebean.annotation.SoftDelete;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.time.Instant;

/**
 * EBean 基础模型。
 * <p>
 * 注意：IDE 应该具备实体增强功能，在 IDEA 中，可以安装 EBean 11.5+ Enhancement 插件。
 *
 * @author mrzhqiang
 */
@Data
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
public abstract class EBeanModel extends Model {

    @Id
    protected Long id;
    @Version
    protected Long version;
    @WhenCreated
    protected Instant created;
    @WhenModified
    protected Instant modified;
    @SoftDelete
    protected boolean deleted;

}
