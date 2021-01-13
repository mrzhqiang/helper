package com.github.mrzhqiang.helper.data.ebean;

import io.ebean.annotation.ReadAudit;
import io.ebean.annotation.WhoCreated;
import io.ebean.annotation.WhoModified;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.MappedSuperclass;

/**
 * 审计模型。
 * <p>
 * 需要实现 CurrentUserProvider 接口。
 * <p>
 * 主要是为了指示当前用户 创建/修改 实体，从而形成审计模型。
 *
 * 对于 {@link ReadAudit} 注解，需要实现 {@link io.ebean.event.readaudit.ReadAuditPrepare} 接口。
 *
 * <pre>
 * class MyReadAuditPrepare implements ReadAuditPrepare {
 *
 *   public void prepare(ReadEvent event) {
 *
 *     // get user context information typically from a
 *     // ThreadLocal or similar mechanism
 *
 *     String currentUserId = ...;
 *     event.setUserId(currentUserId);
 *
 *     String userIpAddress = ...;
 *     event.setUserIpAddress(userIpAddress);
 *
 *     event.setSource("myApplicationName");
 *
 *     // add arbitrary user context information to the
 *     // userContext map
 *     event.getUserContext().put("some", "thing");
 *   }
 * }
 * </pre>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@ReadAudit
public abstract class AuditEBeanModel extends EBeanModel {

    @WhoCreated
    protected String whoCreated;
    @WhoModified
    protected String whoModified;

}
