

import java.util.List;

@CrossOrigin
@RequestMapping("/module")
@RestController
public class ModuleController {

    @Autowired
    private ModuleService moduleService;

    /**
     查询全部菜单（一次性加载）
     */
    @RequestMapping("/menuTree")
    public List<Module> findMenuTree() {
        List<Module> list = moduleService.findTree();
        return list;
    }

    /**
     添加菜单
     @param module
     */
    @PostMapping("/add")
    public Result add(@RequestBody Module module) {

        moduleService.addMenu(module);
        return Result.ok("添加菜单成功");
    }

    /**
     修改菜单
     @param module
     */
    @PutMapping("/edit")
    public Result edit(@RequestBody Module module) {

        moduleService.editMenu(module);
        return Result.ok("修改菜单成功");
    }


    /**
     删除菜单
     @param id
     */
    @DeleteMapping("/delete")
    public Result delete(@RequestParam(name = "id", required = true) String id) {

        moduleService.deleteMenu(id);
        return Result.ok("修改菜单成功");
    }

    /**
     批量删除菜单
     @param ids
     */
    @DeleteMapping("/deleteBach")
    public Result deleteBach(@RequestParam(name = "ids", required = true) String ids) {

        moduleService.deleteBach(ids);
        return Result.ok("删除菜单成功");
    }

}