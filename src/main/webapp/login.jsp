<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>新明机械企业管理系统-登录</title>
	<link href="css/login.css" rel="stylesheet" type="text/css" />
</head>

<body>
<div class="second_body">
	<form method="post" action="login.shtml">
		<div class="logo"></div>
		<div class="title-zh">新明机械企业管理系统</div>
		<span style="color: red">${error}</span>
		<div class="title-en" style="display: none">XMJX Information Manage System</div>
		<div class="message" data-bind="html:message"></div>
		<table border="0" style="width:300px;">
			<tr>
				<td style="white-space:nowrap; padding-bottom: 5px;width:55px;">用户名：</td>
				<td colspan="2"><input type="text" name="username" class="login" data-bind="value:form.userCode" /></td>
			</tr>
			<tr>
				<td class="lable" style="white-space:nowrap; letter-spacing: 0.5em; vertical-align: middle">密码：</td>
				<td colspan="2"><input type="password" name="password" class="login" data-bind="value:form.password" /></td>
			</tr>
			<tr STYLE="display: none;">
				<td></td>
				<td colspan="2"><input type="checkbox" data-bind="checked:form.remember" /><span>系统记住我</span></td>
			</tr>
			<tr>
				<td colspan="3" style="text-align:center">
					<input type="submit" value="登录" class="login_button" />
					<input type="reset" value="重置" class="reset_botton" data-bind="click:resetClick" />
				</td>
			</tr>
		</table>
	</form>
</div>
</body>
</html>