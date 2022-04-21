<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<div>
    <div class="easyui-panel" title="修改登录密码" style="width:400px">
        <div style="padding:10px 0 10px 60px">
                <table>
                    <tr>
                        <td>用户名:</td>
                        <td>${userFormMap.userName}</td>
                    </tr>
                    <tr>
                        <td>原始密码:</td>
                        <td><input class="easyui-validatebox" type="password" id="password" data-options="required:true"></input></td>
                    </tr>
                    <tr>
                        <td>新密码:</td>
                        <td><input class="easyui-validatebox" type="password" id="newpassword" data-options="required:true"></input></td>
                    </tr>
                    <tr>
                        <td>确认密码:</td>
                        <td><input class="easyui-validatebox" type="password" id="surepassword" data-options="required:true"></input></td>
                    </tr>
                </table>
        </div>
        <div style="text-align:center;padding:5px">
            <a href="#" class="easyui-linkbutton" onclick="submit()">确认修改</a>
        </div>
        <script>
            function  submit() {
                var password=getValueById("password");
                var newpassword=getValueById("newpassword");
                var surepassword=getValueById("surepassword");
                if(newpassword==surepassword){
                    var url='${pageContext.request.contextPath}/user/editEntity.shtml';
                    var data={
                        password:password,
                        newpassword:newpassword
                    }
                    communatePost(url,ListToJsonString(data),function back(data){
                        if(data=="error"){
                            showErrorAlert("错误","原始密码错误")
                        }else{
                            showMessagerCenter("恭喜","密码修改成功");
                        }
                    })
                }
                else{
                    showErrorAlert("错误",'两次输入密码不一致,请重新输入');
                }

            }
        </script>
    </div>
</div>
