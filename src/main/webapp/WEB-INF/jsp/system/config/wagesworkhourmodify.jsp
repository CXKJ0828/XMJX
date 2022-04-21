<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<div>
    <div class="easyui-panel" title="设置工资工时" style="width:400px">
        <div style="padding:10px 0 10px 60px">
                <table>
                    <tr>
                        <td>工资/日:</td>
                        <td><input class="easyui-validatebox" type="text" id="dailyWages" data-options="required:true" value="${systemconfigFormMapWages.content}"></input></td>
                    </tr>
                    <tr>
                        <td>工时/日:</td>
                        <td><input class="easyui-validatebox" type="text" id="dailyWorkingHours" data-options="required:true" value="${systemconfigFormMapHours.content}"></input></td>
                    </tr>
                </table>
        </div>
        <div style="text-align:center;padding:5px">
            <a href="#" class="easyui-linkbutton" onclick="submit()">确认修改</a>
        </div>
        <script>
            function  submit() {
                var dailyWages=getValueById("dailyWages");
                var dailyWorkingHours=getValueById("dailyWorkingHours");
                    var url='${pageContext.request.contextPath}/systemconfig/editwagesworkhourEntity.shtml';
                    var data={
                        dailyWages:dailyWages,
                        dailyWorkingHours:dailyWorkingHours
                    }
                    communatePost(url,ListToJsonString(data),function back(data){
                            showMessagerCenter("恭喜","工资工时修改成功");
                    })
            }
        </script>
    </div>
</div>
