<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<div>
    <div class="easyui-panel" title="设置超时设定值" style="width:400px">
        <div style="padding:10px 0 10px 60px">
                <table>
                    <tr>
                        <td>超时设定值(h):</td>
                        <td><input class="easyui-validatebox" type="text" id="timeoutHourCategory" data-options="required:true" value="${systemconfigFormMap.content}"></input></td>
                    </tr>
                </table>
        </div>
        <div style="text-align:center;padding:5px">
            <a href="#" class="easyui-linkbutton" onclick="submit()">确认修改</a>
        </div>
        <script>
            function  submit() {
                var timeoutHourCategory=getValueById("timeoutHourCategory");
                    var url='${pageContext.request.contextPath}/systemconfig/edittimeoutHourCategoryEntity.shtml';
                    var data={
                        timeoutHourCategory:timeoutHourCategory
                    }
                    communatePost(url,ListToJsonString(data),function back(data){
                            showMessagerCenter("恭喜","修改成功");
                    })
            }
        </script>
    </div>
</div>
