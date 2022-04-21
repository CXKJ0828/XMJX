package com.zhjh.controller.system;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhjh.entity.EmployeeFormMap;
import com.zhjh.entity.SystemconfigFormMap;
import com.zhjh.mapper.EmployeeMapper;
import com.zhjh.mapper.SystemconfigMapper;
import com.zhjh.tool.ToolCommon;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhjh.annotation.SystemLog;
import com.zhjh.controller.index.BaseController;
import com.zhjh.entity.UserFormMap;
import com.zhjh.entity.UserGroupsFormMap;
import com.zhjh.exception.SystemException;
import com.zhjh.mapper.UserMapper;
import com.zhjh.util.Common;
import com.zhjh.util.JsonUtils;
import com.zhjh.util.POIUtils;
import com.zhjh.util.PasswordHelper;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 *
 * @author lanyuan 2014-11-19
 * @Email: mmm333zzz520@163.com
 * @version 3.0v
 */
@Controller
@RequestMapping("/user/")
public class UserController extends BaseController {
	@Inject
	private UserMapper userMapper;

	@Inject
	private SystemconfigMapper systemconfigMapper;

	@Inject
	private EmployeeMapper employeeMapper;


	private int listSize;

	@RequestMapping("list")
	public String listUI(Model model) throws Exception {
		UserFormMap userFormMap = getFormMap(UserFormMap.class);
		model.addAttribute("res", findByRes());
		listSize=userMapper.findByWhere(userFormMap).size();
		return Common.BACKGROUND_PATH + "/system/user/list";
	}
	@RequestMapping("employeeSelect")
	public String employeeSelect(Model model) throws Exception {
		return Common.BACKGROUND_PATH + "/system/user/employeeSelect";
	}

	@ResponseBody
	@RequestMapping("findByPage")
	public UserFormMap findByPage( ) throws Exception {
		UserFormMap userFormMap = getFormMap(UserFormMap.class);
		userFormMap=toFormMap(userFormMap, userFormMap.getStr("page"), userFormMap.getStr("rows"),userFormMap.getStr("orderby"));
		List<UserFormMap> roleFormMaps=userMapper.findByPage(userFormMap);
		userFormMap.set("rows",roleFormMaps);
		userFormMap.set("total",listSize);
		return userFormMap;
	}

	@RequestMapping("/export")
	public void download(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String fileName = "用户列表";
		UserFormMap userFormMap = findHasHMap(UserFormMap.class);
		//exportData =
		// [{"colkey":"sql_info","name":"SQL语句","hide":false},
		// {"colkey":"total_time","name":"总响应时长","hide":false},
		// {"colkey":"avg_time","name":"平均响应时长","hide":false},
		// {"colkey":"record_time","name":"记录时间","hide":false},
		// {"colkey":"call_count","name":"请求次数","hide":false}
		// ]
		String exportData = userFormMap.getStr("exportData");// 列表头的json字符串

		List<Map<String, Object>> listMap = JsonUtils.parseJSONList(exportData);

		List<UserFormMap> lis = userMapper.findUserPage(userFormMap);
		POIUtils.exportToExcel(response, listMap, lis, fileName,"sheet1");
	}

	@RequestMapping("addUI")
	public String addUI(Model model,HttpServletRequest request) throws Exception {
		return Common.BACKGROUND_PATH + "/system/user/add";
	}
	@RequestMapping("modify")
	public String modify(Model model) throws Exception {
		// 获取登录的bean
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		UserFormMap userFormMap = (UserFormMap)Common.findUserSession(request);
		// 登陆的信息回传页面
		model.addAttribute("userFormMap", userFormMap);
		return Common.BACKGROUND_PATH + "/system/user/modify";
	}

	@RequestMapping("modifydelete")
	public String modifydelete(Model model) throws Exception {
		return Common.BACKGROUND_PATH + "/system/user/modifydelete";
	}

	@ResponseBody
	@RequestMapping("addEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统管理",methods="用户管理-新增用户")//凡需要处理业务逻辑的.都需要记录操作日志
	public String addEntity(String txtGroupsSelect) throws Exception {
		try {
			UserFormMap userFormMap = getFormMap(UserFormMap.class);
			userFormMap.put("txtGroupsSelect", txtGroupsSelect);
			PasswordHelper passwordHelper = new PasswordHelper();
			userFormMap.set("password","123456789");
			passwordHelper.encryptPassword(userFormMap);
			userMapper.addEntity(userFormMap);//新增后返回新增信息
			if (!Common.isEmpty(txtGroupsSelect)) {
				String[] txt = txtGroupsSelect.split(",");
				UserGroupsFormMap userGroupsFormMap = new UserGroupsFormMap();
				for (String roleId : txt) {
					userGroupsFormMap.put("userId", userFormMap.get("id"));
					userGroupsFormMap.put("roleId", roleId);
					userMapper.addEntity(userGroupsFormMap);
				}
			}
		} catch (Exception e) {
			throw new SystemException("添加账号异常");
		}
		return "success";
	}

	@ResponseBody
	@RequestMapping("userSelect")
	public UserFormMap userSelect(String q) throws Exception {
		UserFormMap  userFormMap  = getFormMap(UserFormMap .class);
		userFormMap.set("content",q);
		List<UserFormMap> departmentFormMapList=userMapper.findByUserNameAndAccountName(userFormMap);
		UserFormMap bomFormMap=new UserFormMap();
		bomFormMap.set("rows",departmentFormMapList);
		return bomFormMap;
	}
	@ResponseBody
	@RequestMapping("sureCompleteUserSelect")
	public UserFormMap sureCompleteUserSelect(String q) throws Exception {
		UserFormMap  userFormMap  = getFormMap(UserFormMap .class);
		userFormMap.set("content",q);
		userFormMap.set("wages","确认完成人");
		List<UserFormMap> departmentFormMapList=userMapper.findByWagesNameAndAccountName(userFormMap);
		UserFormMap bomFormMap=new UserFormMap();
		bomFormMap.set("rows",departmentFormMapList);
		return bomFormMap;
	}

	@ResponseBody
	@RequestMapping("checkUserSelect")
	public UserFormMap checkUserSelect(String q) throws Exception {
		UserFormMap  userFormMap  = getFormMap(UserFormMap .class);
		userFormMap.set("content",q);
		userFormMap.set("wages","检验确认人");
		List<UserFormMap> departmentFormMapList=userMapper.findByWagesNameAndAccountName(userFormMap);
		UserFormMap bomFormMap=new UserFormMap();
		bomFormMap.set("rows",departmentFormMapList);
		return bomFormMap;
	}

	@ResponseBody
	@RequestMapping("userSelectHeatTreat")
	public UserFormMap userSelectHeatTreat(String q,String origin,String remarks) throws Exception {
		List<UserFormMap> departmentFormMapList=new ArrayList<>();
		UserFormMap  userFormMap  = getFormMap(UserFormMap .class);
		userFormMap.set("content",q);
		if(origin.equals("渗碳")
				||origin.equals("外圆粗磨")
				||origin.equals("端面平磨")
				||origin.equals("消差磨")
				||origin.equals("统一尺寸")
				||origin.equals("内孔磨")
				||origin.equals("外圆精磨")
				||origin.equals("外圆磨（轴）")
				||origin.equals("平磨（垫片）")
				||origin.equals("外磨内磨端面")
				||origin.equals("车后")
				||origin.equals("打磨")
				){
			if(origin.equals("渗碳")&&remarks.equals("外圆粗磨")){
				remarks="外圆粗磨";
			}else if(origin.equals("渗碳")&&remarks.equals("外圆磨（轴）")){
				remarks="外圆磨（轴）";
			}else if(origin.equals("渗碳")&&remarks.equals("平磨（垫片）")){
				remarks="平磨（垫片）";
			}else if(origin.equals("外磨内磨端面")){
				remarks="外磨+（垫片）";
			}else if(origin.equals("车后")&&remarks.equals("销轴")){
				remarks="车后（轴）";
			}else if(origin.equals("车后")&&remarks.equals("钢套")){
				remarks="车后（套）";
			}else if(origin.equals("车后")&&remarks.equals("垫")){
				remarks="车后（垫）";
			}else if(origin.equals("打磨")){
				remarks="打磨";
			}else if(origin.equals("渗碳")&&!remarks.equals("外圆粗磨")){
				remarks="";
			}else{
				remarks=origin;
			}

		}else{
			remarks="";
		}
		userFormMap.set("departmentName",remarks);
		if(origin.equals("调质")){
			UserFormMap userFormMapNow=getNowUserMessage();
			String roleName=userFormMapNow.get("roleName")+"";
			if(roleName.equals("分管理员")){
				UserFormMap userFormMap1=new UserFormMap();
				userFormMap1.set("id","506");
				userFormMap1.set("accountName","1069");
				userFormMap1.set("userName","宋银兵");
				departmentFormMapList.add(userFormMap1);

				UserFormMap userFormMap2=new UserFormMap();
				userFormMap2.set("id","545");
				userFormMap2.set("accountName","1092");
				userFormMap2.set("userName","刘欣");
				departmentFormMapList.add(userFormMap2);
			}else{
				departmentFormMapList=userMapper.findByUserNameAndAccountName(userFormMap);
			}
		}else{
			departmentFormMapList=userMapper.findByUserNameAndAccountName(userFormMap);
		}


		UserFormMap bomFormMap=new UserFormMap();
		bomFormMap.set("rows",departmentFormMapList);
		return bomFormMap;
	}

	@ResponseBody
	@RequestMapping("deleteEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统管理",methods="用户管理-删除用户")//凡需要处理业务逻辑的.都需要记录操作日志
	public String deleteEntity() throws Exception {
		String[] ids = getParaValues("ids");
		for (String id : ids) {
			userMapper.deleteByAttribute("userId", id, UserGroupsFormMap.class);
//			userMapper.deleteByAttribute("userId", id, ResUserFormMap.class);
			userMapper.deleteByAttribute("id", id, UserFormMap.class);
		}
		return "success";
	}

	@RequestMapping("editUI")
	public String editUI(Model model,HttpServletRequest request) throws Exception {
		String id = getPara("id");
		if(Common.isNotEmpty(id)){
			model.addAttribute("user", userMapper.findbyFrist("id", id, UserFormMap.class));
		}
		return Common.BACKGROUND_PATH + "/system/user/edit";
	}

	@ResponseBody
	@RequestMapping("editEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统管理",methods="用户管理-修改用户")//凡需要处理业务逻辑的.都需要记录操作日志
	public String editEntity(String entity) throws Exception {
		String password= ToolCommon.json2Object(entity).getString("password");
		String newpassword=ToolCommon.json2Object(entity).getString("newpassword");
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		UserFormMap userFormMap = (UserFormMap)Common.findUserSession(request);
		Subject user = SecurityUtils.getSubject();
		// 用户输入的账号和密码,,存到UsernamePasswordToken对象中..然后由shiro内部认证对比,
		// 认证执行者交由ShiroDbRealm中doGetAuthenticationInfo处理
		// 当以上认证成功后会向下执行,认证失败会抛出异常
		UsernamePasswordToken token = new UsernamePasswordToken(userFormMap.getStr("accountName"), password);
		try {
			user.login(token);
			PasswordHelper passwordHelper = new PasswordHelper();
			userFormMap.set("password",newpassword);
			passwordHelper.encryptPassword(userFormMap);
			userMapper.editEntity(userFormMap);//新增后返回新增信息
			return "success";

		} catch (Exception lae) {
			return "error";
		}


	}

	@ResponseBody
	@RequestMapping("editDeleteEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统管理",methods="用户管理-修改用户")//凡需要处理业务逻辑的.都需要记录操作日志
	public String editDeleteEntity(String entity) throws Exception {
		String password= ToolCommon.json2Object(entity).getString("password");
		String newpassword=ToolCommon.json2Object(entity).getString("newpassword");
		SystemconfigFormMap systemconfigFormMap=new SystemconfigFormMap();
		systemconfigFormMap.set("name","deletepassword");
		systemconfigFormMap.set("content",password);
		List<SystemconfigFormMap> systemconfigFormMaps=systemconfigMapper.findByNames(systemconfigFormMap);
		if(systemconfigFormMaps.size()>0){
			SystemconfigFormMap systemconfigFormMap1=systemconfigFormMaps.get(0);
			systemconfigFormMap1.set("content",newpassword);
			systemconfigMapper.editEntity(systemconfigFormMap1);
			return "success";
		}else{
			return "error";
		}


	}

	/**
	 * 验证账号是否存在
	 *
	 * @author lanyuan Email：mmm333zzz520@163.com date：2014-2-19
	 * @param name
	 * @return
	 */
	@RequestMapping("isExist")
	@ResponseBody
	public boolean isExist(String name) {
		//在user表里查看是否存在该accountName
		UserFormMap account = userMapper.findbyFrist("accountName", name, UserFormMap.class);
		if (account == null) {//不存在
			//在employee表里查看是否存在该employeeId
			EmployeeFormMap employeeFormMap=employeeMapper.findbyFrist("employeeId",name,EmployeeFormMap.class);
			if (employeeFormMap == null) {//不存在 无法添加
//				result="所输入账号无相应员工信息，请重新输入或添加员工后继续操作";
				return false;
			} else {
				return true;
			}
		} else {
//			result="该账号已经存在";
			return false;
		}
	}

	/**
	 * 验证账号在员工表是否存在
	 *
	 * @author lanyuan Email：mmm333zzz520@163.com date：2014-2-19
	 * @param name
	 * @return
	 */
	@RequestMapping("isExistInEmployee")
	@ResponseBody
	public boolean isExistInEmployee(String name) {
		EmployeeFormMap employeeFormMap=employeeMapper.findbyFrist("employeeId",name,EmployeeFormMap.class);
		if (employeeFormMap == null) {
			return true;
		} else {
			return false;
		}
	}

	//密码修改
	@RequestMapping("updatePassword")
	public String updatePassword(Model model) throws Exception {
		return Common.BACKGROUND_PATH + "/system/user/updatePassword";
	}

	//保存新密码
	@RequestMapping("editPassword")
	@ResponseBody
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统管理",methods="用户管理-修改密码")//凡需要处理业务逻辑的.都需要记录操作日志
	public String editPassword() throws Exception{
		// 当验证都通过后，把用户信息放在session里
		UserFormMap userFormMap = getFormMap(UserFormMap.class);
		userFormMap.put("password", userFormMap.get("newpassword"));
		//这里对修改的密码进行加密
		PasswordHelper passwordHelper = new PasswordHelper();
		passwordHelper.encryptPassword(userFormMap);
		userMapper.editEntity(userFormMap);
		return "success";
	}
}