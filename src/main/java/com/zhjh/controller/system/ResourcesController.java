package com.zhjh.controller.system;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.zhjh.entity.*;
import com.zhjh.mapper.ButtomMapper;
import com.zhjh.mapper.ResRoleMapper;
import com.zhjh.tool.ToolCommon;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhjh.annotation.SystemLog;
import com.zhjh.controller.index.BaseController;
import com.zhjh.mapper.ResourcesMapper;
import com.zhjh.util.Common;
import com.zhjh.util.TreeObject;
import com.zhjh.util.TreeUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 
 * @author lanyuan 2014-11-19
 * @Email: mmm333zzz520@163.com
 * @version 3.0v
 */
@Controller
@RequestMapping("/resources/")
public class ResourcesController extends BaseController {
	@Inject
	private ResourcesMapper resourcesMapper;
	@Inject
	private ResRoleMapper resRoleMapper;
	@Inject
	private ButtomMapper buttomMapper;
	public String roleId;
	/**
	 * @param model
	 *            存放返回界面的model
	 * @return
	 */
	@ResponseBody
	@RequestMapping("treelists")
	public ResFormMap findByPage(Model model) {
		ResFormMap resFormMap = getFormMap(ResFormMap.class);
		String order1 = " order by level asc";
		resFormMap.put("$orderby", order1);
		List<ResFormMap> mps = resourcesMapper.findByNames(resFormMap);
		List<TreeObject> list = new ArrayList<TreeObject>();
		for (ResFormMap map : mps) {
			TreeObject ts = new TreeObject();
			Common.flushObject(ts, map);
			list.add(ts);
		}
		TreeUtil treeUtil = new TreeUtil();
		List<TreeObject> ns = treeUtil.getChildTreeObjects(list, 0);
		resFormMap = new ResFormMap();
		resFormMap.put("rows", ns);
		return resFormMap;
	}

	@ResponseBody
	@RequestMapping("treelistsByParentId")
	public ResFormMap treelistsByParentId(Model model,String parentId) {
		ResFormMap resFormMap = getFormMap(ResFormMap.class);
		resFormMap.put("parentId", parentId);
		List<ResFormMap> mps = resourcesMapper.findByNames(resFormMap);
		resFormMap = new ResFormMap();
		resFormMap.put("rows", mps);
		return resFormMap;
	}

	@ResponseBody
	@RequestMapping("reslists")
	public List<TreeObject> reslists(Model model) throws Exception {
		ResFormMap resFormMap = getFormMap(ResFormMap.class);
		List<ResFormMap> mps = resourcesMapper.findByWhere(resFormMap);
		List<TreeObject> list = new ArrayList<TreeObject>();
		for (ResFormMap map : mps) {
			TreeObject ts = new TreeObject();
			Common.flushObject(ts, map);
			list.add(ts);
		}
		TreeUtil treeUtil = new TreeUtil();
		List<TreeObject> ns = treeUtil.getChildTreeObjects(list, 0, "　");
		return ns;
	}

	/**
	 * @param model
	 *            存放返回界面的model
	 * @return
	 */
	@RequestMapping("list")
	public String list(Model model) {
		model.addAttribute("res", findByRes());
		return Common.BACKGROUND_PATH + "/system/resources/list";
	}

	/**
	 * 跳转到修改界面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("editUI")
	public String editUI(Model model) {
		String id = getPara("id");
		if(Common.isNotEmpty(id)){
			model.addAttribute("resources", resourcesMapper.findbyFrist("id", id, ResFormMap.class));
		}
		return Common.BACKGROUND_PATH + "/system/resources/edit";
	}

	/**
	 * 跳转到新增界面
	 * 
	 * @return
	 */
	@RequestMapping("addUI")
	public String addUI(Model model) {
		model.addAttribute("listbutton",buttomMapper.findByWhere(new ButtomFormMap()));
		return Common.BACKGROUND_PATH + "/system/resources/add";
	}

	/**
	 * 权限分配页面
	 * 
	 * @author lanyuan Email：mmm333zzz520@163.com date：2014-4-14
	 * @param model
	 * @return
	 */
	@RequestMapping("permissionsOpration")
	public String permissionsOpration(Model model) {
		roleId=getPara("roleId");
		return Common.BACKGROUND_PATH + "/system/resources/permissions";
	}

    /**
     * 权限分配页面
     *
     * @author lanyuan Email：mmm333zzz520@163.com date：2014-4-14
     * @return
     */
    @ResponseBody
    @RequestMapping("permissionsEntity")
    @Transactional(readOnly=false)//需要事务操作必须加入此注解
    public List permissionsEntity() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		UserFormMap userFormMap = (UserFormMap)Common.findUserSession(request);
		ResFormMap resFormMap = new ResFormMap();
		resFormMap.put("userId", userFormMap.get("id"));
		List<ResFormMap> mps = resourcesMapper.findRes(resFormMap);
		if(mps.size()==0){
			mps = resourcesMapper.findResByEmployee(resFormMap);
		}
		List<TreeObject> list = new ArrayList<TreeObject>();
		for (ResFormMap map : mps) {
			TreeObject ts = new TreeObject();
			Common.flushObject(ts, map);
			list.add(ts);
		}
		TreeUtil treeUtil = new TreeUtil();
        List<TreeObject> ns = treeUtil.getChildTreeObjects(list, 0);
        return ns;
    }

	@ResponseBody
	@RequestMapping("navEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public List navEntity() {
		ResFormMap resFormMap = getFormMap(ResFormMap.class);
		List<ResFormMap> mps = resourcesMapper.findByWhere(resFormMap);
		List<TreeObject> list = new ArrayList<TreeObject>();
		for (ResFormMap map : mps) {
			TreeObject ts = new TreeObject();
			Common.flushObject(ts, map);
			list.add(ts);
		}
		TreeUtil treeUtil = new TreeUtil();
		List<TreeObject> ns = treeUtil.getChildTreeObjects(list, 0);
		return ns;
	}

	/**
	 * 添加菜单
	 * 
	 * @throws Exception
	 */
	@RequestMapping("addEntity")
	@ResponseBody
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统管理",methods="资源管理-新增资源")//凡需要处理业务逻辑的.都需要记录操作日志
	public String addEntity(String entity) throws Exception {
		ResFormMap resFormMap = getFormMap(ResFormMap.class);
		if("2".equals(resFormMap.get("type"))){//类型为按钮
			String description=resFormMap.getStr("description");
			if(Common.isNumeric1(description)){
				ButtomFormMap buttomFormMap=new ButtomFormMap();
				buttomFormMap.set("id",description);
				List<ButtomFormMap> buttomFormMapList=buttomMapper.findByNames(buttomFormMap);
				description=buttomFormMapList.get(0).getStr("buttom");
			}
			resFormMap.put("description", Common.htmltoString(description+""));
		}
		Object o = resFormMap.get("ishide");
		if(null==o){
			resFormMap.set("ishide", "0");
		}

		resourcesMapper.addEntity(resFormMap);
		ResRoleFormMap resRoleFormMap=new ResRoleFormMap();
		resRoleFormMap.set("resId",resFormMap.get("id"));
		resRoleFormMap.set("roleId",1);
		resRoleMapper.addEntity(resRoleFormMap);
		result="success";
		return result;
	}

	/**
	 * 更新菜单
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("editEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统管理",methods="资源管理-修改资源")//凡需要处理业务逻辑的.都需要记录操作日志
	public String editEntity(Model model,String entity) throws Exception {
		List<ResFormMap> list = (List) ToolCommon.json2ObjectList(entity, ResFormMap.class);
		for(int i=0;i<list.size();i++){
			ResFormMap resFormMap=list.get(i);
			Object o = resFormMap.get("ishide");
			if(null==o){
				resFormMap.set("ishide", "0");
			}
			resourcesMapper.editEntity(resFormMap);
		}
		return "success";
	}

	/**
	 * 根据ID删除菜单
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("deleteEntity")
	@SystemLog(module="系统管理",methods="资源管理-删除资源")//凡需要处理业务逻辑的.都需要记录操作日志
	public String deleteEntity(Model model,String entity) throws Exception {
		String[] ids = entity.split(",");
		for (String id : ids) {
			resourcesMapper.deleteByAttribute("id", id, ResFormMap.class);
		};
		return "success";
	}

	@RequestMapping("sortUpdate")
	@ResponseBody
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	public String sortUpdate(Params params) throws Exception {
		List<String> ids = params.getId();
		List<String> es = params.getRowId();
		List<ResFormMap> maps = new ArrayList<ResFormMap>();
		for (int i = 0; i < ids.size(); i++) {
			ResFormMap map = new ResFormMap();
			map.put("id", ids.get(i));
			map.put("level", es.get(i));
			maps.add(map);
		}
		resourcesMapper.updateSortOrder(maps);
		return "success";
	}

	@ResponseBody
	@RequestMapping("findRes")
	public List<ResFormMap> findUserRes() {
		ResFormMap resFormMap = getFormMap(ResFormMap.class);
		List<ResFormMap> rs = resourcesMapper.findRes(resFormMap);
		return rs;
	}
	@ResponseBody
	@RequestMapping("addUserRes")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统管理",methods="用户管理/组管理-修改权限")//凡需要处理业务逻辑的.都需要记录操作日志
	public String addUserRes(String ids) throws Exception {
		/*
		 * Liuyang更改用户授权为角色授权
		 */
		resourcesMapper.deleteByAttribute("roleId", roleId, ResRoleFormMap.class);
		String[] s =ids.split(",");
		List<ResRoleFormMap> resRoleFormMaps = new ArrayList<ResRoleFormMap>();
		for (String rid : s) {
			ResRoleFormMap resRoleFormMap = new ResRoleFormMap();
			resRoleFormMap.put("resId", rid);
			resRoleFormMap.put("roleId", roleId);
			resRoleFormMaps.add(resRoleFormMap);
		
		}
		resourcesMapper.batchSave(resRoleFormMaps);
		return "success";
		
	}

	@ResponseBody
	@RequestMapping("findByButtom")
	public List<ButtomFormMap> findByButtom(){
		return resourcesMapper.findByWhere(new ButtomFormMap());
	}
	
	/**
	 * 验证菜单是否存在
	 * 
	 * @param name
	 * @return
	 */
	@RequestMapping("isExist")
	@ResponseBody
	public boolean isExist(String name,String resKey) {
		ResFormMap resFormMap = getFormMap(ResFormMap.class);
		List<ResFormMap> r = resourcesMapper.findByNames(resFormMap);
		if (r.size()==0) {
			return true;
		} else {
			return false;
		}
	}
}