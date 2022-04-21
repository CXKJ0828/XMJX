<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<div>
    <div class="easyui-panel" title="设置加班工资" style="width:400px">
        <div style="padding:10px 0 10px 60px">
                <table>
                    <tr>
                        <td>加班工资:</td>
                        <td><input class="easyui-validatebox" type="text" id="workoverwage" data-options="required:true" value="${systemconfigFormMapWages.content}"></input></td>
                    </tr>
                </table>
        </div>
        <div style="text-align:center;padding:5px">
            <a href="#" class="easyui-linkbutton" onclick="submit()">确认修改</a>
        </div>
        <script>
            function  submit() {
                var workoverwage=getValueById("workoverwage");
                    var url='${pageContext.request.contextPath}/systemconfig/editworkoverWageEntity.shtml';
                    var data={
                        workoverwage:workoverwage
                    }
                    communatePost(url,ListToJsonString(data),function back(data){
                            showMessagerCenter("恭喜","加班工资修改成功");
                    })
            }
        </script>
    </div>
</div>
