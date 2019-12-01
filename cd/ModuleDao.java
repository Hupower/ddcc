
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Dao接口
 * @author Harry Hu
 * 添加@Mapper注解是为了给SpringBoot扫描Mapper接口生成代理对象，存入IOC容器
 */
@Mapper
public interface ModuleDao extends BaseMapper<Module> {

    //查询所有菜单
    List<Module> findTree();

    /**
     *   修改菜单状态字段： 是否子节点
     */
    @Update("update tb_module set is_leaf=#{leaf} where id = #{id}")
    public int setMenuLeaf(@Param("id") String id, @Param("leaf") int leaf);


}
