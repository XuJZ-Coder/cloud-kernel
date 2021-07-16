package cn.zyszkj.cloud.kernel.core.metadata;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 *  自定义sql字段填充器
 *
 * @author XuJZ
 */
public class CustomMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.fillStrategy(metaObject,getIsDelFieldName(),getDefaultIsDelValue());
        this.fillStrategy(metaObject,getIsEnableFieldName(),getDefaultIsEnableValue());
        this.fillStrategy(metaObject,getIsSyncFieldName(),getDefaultIsSyncValue());
        this.fillStrategy(metaObject,getCreateTimeFieldName(), new Date());
        this.fillStrategy(metaObject,getCreateUserFieldName(),getUserUniqueId());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.fillStrategy(metaObject,getUpdateTimeFieldName(), new Date());
        this.fillStrategy(metaObject,getUpdateUserFieldName(),getUserUniqueId());
    }

    /**
     * 获取是否启用字段的名称（非数据库中字段名称）
     */
    protected String getIsEnableFieldName() {
        return "isEnable";
    }

    /**
     * 获取是否同步字段的默认值
     */
    protected Integer getDefaultIsEnableValue() {
        return 1;
    }

    /**
     * 获取逻辑删除字段的名称（非数据库中字段名称）
     */
    protected String getIsDelFieldName() {
        return "isDel";
    }

    /**
     * 获取逻辑删除字段的默认值
     */
    protected Integer getDefaultIsDelValue() {
        return 0;
    }

    /**
     * 获取创建时间字段的名称（非数据库中字段名称）
     */
    protected String getCreateTimeFieldName() {
        return "createTime";
    }

    /**
     * 获取创建用户字段的名称（非数据库中字段名称）
     */
    protected String getCreateUserFieldName() {
        return "createUser";
    }

    /**
     * 获取更新时间字段的名称（非数据库中字段名称）
     */
    protected String getUpdateTimeFieldName() {
        return "updateTime";
    }

    /**
     * 获取更新用户字段的名称（非数据库中字段名称）
     */
    protected String getUpdateUserFieldName() {
        return "updateUser";
    }

    /**
     * 获取用户唯一id（注意默认获取的用户唯一id为空，如果想填写则需要继承本类）
     */
    protected Long getUserUniqueId() {
        return -100L;
    }

    /**
     * 获取是否同步字段的名称（非数据库中字段名称）
     */
    protected String getIsSyncFieldName() {
        return "isSync";
    }

    /**
     * 获取是否同步字段的默认值
     */
    protected Integer getDefaultIsSyncValue() {
        return 0;
    }
}
