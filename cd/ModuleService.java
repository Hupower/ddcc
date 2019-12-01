

import java.util.List;

public interface ModuleService extends BaseService<Module> {


    //查询所有菜单
    List<Module> findTree();


    //添加菜单
    void addMenu(Module module);

    //修改菜单
    void editMenu(Module module);

    //删除菜单
    void deleteMenu(String id);
}
