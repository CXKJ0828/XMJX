package com.zhjh.controller.mobile;

import com.zhjh.bean.LoginBean;
import com.zhjh.controller.index.BaseController;
import com.zhjh.entity.*;
import com.zhjh.mapper.*;
import com.zhjh.tool.ToolCommon;
import com.zhjh.util.Common;
import com.zhjh.util.PasswordHelper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 进行管理后台框架界面的类
 * 
 * @author lanyuan 2015-04-05
 * @Email: mmm333zzz520@163.com
 * @version 3.0v
 * @mod  Ekko 2015-09-07
 */
@Controller
@RequestMapping("/")
public class IndexController extends BaseController {

	@Inject
	private UserMapper userMapper;

	@Inject
	private AccountMapper accountMapper;

	@Inject
	private UserLoginMapper userLoginMapper;

	@Inject
	private CidRoleMapper cidRoleMapper;
	@Inject
	private RoleMapper roleMapper;
	@Inject
	private AppConfigMapper appConfigMapper;

	@Inject
	private OrderDetailsProcessMapper orderDetailsProcessMapper;


	@ResponseBody
	@RequestMapping(value = "loginM", method = RequestMethod.GET, produces = "text/html; charset=utf-8")
	public LoginBean loginM(HttpServletRequest request,String username, String password) {
		username= ToolCommon.changeContentFromHtmlMessyCode(username);
		password= ToolCommon.changeContentFromHtmlMessyCode(password);
		LoginBean loginBean=new LoginBean();
		if (Common.isEmpty(username) || Common.isEmpty(password)) {
			loginBean.set("id",0);
			loginBean.set("result","用户名或密码不能为空！");
		}else{
			Subject user = SecurityUtils.getSubject();
			UsernamePasswordToken token = new UsernamePasswordToken(username, password);
			try {
				user.login(token);
				UserLoginFormMap userLogin = new UserLoginFormMap();
				Session session = SecurityUtils.getSubject().getSession();

				UserFormMap userFormMap= (UserFormMap) session.getAttribute("userSession");
				String isAutoMakecarburization="";
				if(username.equals("0006")||username.equals("5556")){
					isAutoMakecarburization="渗碳中频成品";
				}
				if(username.equals("0007")){
					isAutoMakecarburization="渗碳成品";
				}
				if(username.equals("0009")||username.equals("1075")||username.equals("1012")){
					isAutoMakecarburization="成品";
				}
				userFormMap.set("isAutoMakecarburization",isAutoMakecarburization);
				String userId=userFormMap.get("id")+"";
				RoleFormMap roleFormMap=new RoleFormMap();
				roleFormMap.set("userId",userId);
				RoleFormMap roleFormMap1=roleMapper.seletUserRoleByUserId(roleFormMap);
				userFormMap.set("roleId",roleFormMap1.get("roleId")+"");
				//获取site对象传入到session中
				userLogin.put("userEntity",userFormMap);
				loginBean.set("id",1);
				loginBean.set("result","登录成功！");
				loginBean.set("loginBean",userLogin);
			} catch (LockedAccountException lae) {
				token.clear();
				loginBean.set("id",0);
				loginBean.set("result","用户已经被锁定不能登录，请与管理员联系！");
			} catch (ExcessiveAttemptsException e) {
				token.clear();
				loginBean.set("id",0);
				loginBean.set("result","账号：" + username + " 登录失败次数过多,锁定10分钟!");
			} catch (AuthenticationException e) {
				token.clear();
				AccountFormMap accountFormMap=new AccountFormMap();
				accountFormMap.set("username",username);

				List<AccountFormMap> accountFormMaps=accountMapper.findByNames(accountFormMap);
				if(accountFormMaps==null||accountFormMaps.size()==0){
					loginBean.set("id",0);
					loginBean.set("result","用户或密码不正确！");
				}else{
					accountFormMap.set("password",password);
					List<AccountFormMap> accountFormMaps1=accountMapper.findByNames(accountFormMap);
					if(accountFormMaps1==null||accountFormMaps1.size()==0){
						loginBean.set("id",0);
						loginBean.set("result","用户或密码不正确！");
					}else{
						loginBean.set("id",0);
						loginBean.set("result","该账户处于审核中，暂不可登录");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				loginBean.set("id",0);
				loginBean.set("result",e.getMessage());
			}
		}
		return loginBean;
	}

	@ResponseBody
	@RequestMapping("getAndroidMessageM")
	public AppConfigFormMap getAndroidMessageM() throws Exception {
		AppConfigFormMap appConfigFormMapResult=new AppConfigFormMap();
		AppConfigFormMap appConfigFormMap=appConfigMapper.findbyFrist("origin","android",AppConfigFormMap.class);
		appConfigFormMapResult.set("id",1);
		appConfigFormMapResult.set("result","操作成功！");
		appConfigFormMapResult.set("entity",appConfigFormMap);
		return appConfigFormMapResult;
	}

	@ResponseBody
	@RequestMapping(value = "modifypasswordM", method = RequestMethod.GET, produces = "text/html; charset=utf-8")
	public LoginBean modifypasswordM(HttpServletRequest request,String username,
									 String oldpassword,
									 String newpassword,
									 String surepassword) {
		username= ToolCommon.changeContentFromHtmlMessyCode(username);
		oldpassword= ToolCommon.changeContentFromHtmlMessyCode(oldpassword);
		newpassword= ToolCommon.changeContentFromHtmlMessyCode(newpassword);
		surepassword= ToolCommon.changeContentFromHtmlMessyCode(surepassword);
		LoginBean loginBean=new LoginBean();
		if (Common.isEmpty(username) || Common.isEmpty(oldpassword)) {
			loginBean.set("id",0);
			loginBean.set("result","原始密码不能为空！");
		}else{
			Subject user = SecurityUtils.getSubject();
			UsernamePasswordToken token = new UsernamePasswordToken(username, oldpassword);
			try {
				user.login(token);
				Session session = SecurityUtils.getSubject().getSession();
				UserFormMap userFormMap= (UserFormMap) session.getAttribute("userSession");
				PasswordHelper passwordHelper = new PasswordHelper();
				userFormMap.set("password",newpassword);
				passwordHelper.encryptPassword(userFormMap);
				userMapper.editEntity(userFormMap);
				loginBean.set("id",1);
				loginBean.set("result","密码修改成功！");
			} catch (LockedAccountException lae) {
				token.clear();
				loginBean.set("id",0);
				loginBean.set("result","用户已经被锁定不能登录，请与管理员联系！");
			} catch (ExcessiveAttemptsException e) {
				token.clear();
				loginBean.set("id",0);
				loginBean.set("result","账号：" + username + " 登录失败次数过多,锁定10分钟!");
			} catch (AuthenticationException e) {
				token.clear();
				loginBean.set("id",0);
				loginBean.set("result","旧密码不正确！");
			} catch (Exception e) {
				e.printStackTrace();
				loginBean.set("id",0);
				loginBean.set("result","登录异常，请联系管理员！");
			}
		}
		return loginBean;
	}

}
