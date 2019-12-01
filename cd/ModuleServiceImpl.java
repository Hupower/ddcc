
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ModuleServiceImpl extends
        BaseServiceImpl<Module> implements ModuleService {

    //@Resource
    @Autowired
    private ModuleDao moduleDao;


    @Override
    public List<Module> findTree() {

        try {
            // 加载所有菜单列表
            List<Module> allMenu = moduleDao.findTree();

            //根级的菜单列表
            List<Module> rootList = new ArrayList<>();
            for (Module nav : allMenu) {
                if (nav.getParentId().equals("0")) {//父节点是0的，为根节点。
                    rootList.add(nav);
                }
            }

            // 循环根级菜单，为每个根级菜单下的子菜单赋值。
            for (Module module : rootList) {
                module.setChildNodes(getChildNodes(module.getId(), allMenu));
            }
            return rootList;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList();
        }
    }

    /**
     Return the list of children node from percent menu id
     @param id current menu id
     @param allMenu all list of menus
     */
    private List<Module> getChildNodes(String id, List<Module> allMenu) {
        // The list of child nodes
        List<Module> childList = new ArrayList<>();
        // Fill the list of child'nodes which parent id equal params of id
        for (Module module : allMenu) {
            if (StringUtils.isNotBlank(module.getParentId())) {
                if (id.equals(module.getParentId())) {
                    childList.add(module);
                }
            }
        }
        //递归
        for (Module entity : childList) {
            entity.setChildNodes(getChildNodes(entity.getId(), allMenu));
        }

        if (childList.size() == 0) {
            return new ArrayList<Module>();
        }
        return childList;
    }


    @Override
    public void addMenu(Module module) {

        //判断是否是一级菜单，是的话清空父菜单
        if (module.getCtype() == 0) {
            module.setParentId(null);
        }

        String pid = module.getParentId();
        if (StringUtils.isNotBlank(pid)) {
            //设置父节点不为叶子节点
            moduleDao.setMenuLeaf(pid, 0);
        }
        module.setIsLeaf(0);
        moduleDao.insertSelective(module);

    }

    @Override
    public void editMenu(Module module) {

        Module p = moduleDao.selectByPrimaryKey(module.getId());

        //TODO 该节点判断是否还有子节点
        if(p==null) {
            throw new RuntimeException("未找到菜单信息");
        }else {
            //----------------------------------------------------------------------
            //Step1.判断是否是一级菜单，是的话清空父菜单ID
            if(module.getCtype()== 0) {
                module.setParentId("");
            }
            //Step2.判断菜单下级是否有菜单，无则设置为叶子节点
            if (module.getCtype() == 1){
                module.setIsLeaf(1);
            }
            if (module.getCtype() == 2){
                module.setIsLeaf(2);
            }
            this.update(module);
        }
    }

    @Override
    public void deleteMenu(String id) {

        Module sysPermission = moduleDao.selectByPrimaryKey(id);

        if(sysPermission==null) {
            throw new RuntimeException("未找到菜单信息");
        }
        String pid = sysPermission.getParentId();
        if(StringUtils.isNotBlank(pid)) {
            int count = this.count(new QueryWrapper<SysPermission>().lambda().eq(SysPermission::getParentId, pid));
            if(count==1) {
                //若父节点无其他子节点，则该父节点是叶子节点
                this.sysPermissionMapper.setMenuLeaf(pid, 1);
            }
        }
        moduleDao.deleteById(id);
        // 该节点可能是子节点但也可能是其它节点的父节点,所以需要级联删除
        this.removeChildrenBy(sysPermission.getId());
    }
}
