<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<div>
    <div class="easyui-panel" title="设置扣款基础值" style="width:400px">
        <div style="padding:10px 0 10px 60px">
                <table>
                    <tr>
                        <td>扣款基础值(元):</td>
                        <td><input class="easyui-validatebox" type="text" id="deductionBaseValue" data-options="required:true" value="${systemconfigFormMap.content}"></input></td>
                    </tr>
                </table>
        </div>
        <div style="text-align:center;padding:5px">
            <a href="#" class="easyui-linkbutton" onclick="submit()">确认修改</a>
        </div>
        <script>
            function  submit() {
                var deductionBaseValue=getValueById("deductionBaseValue");
                    var url='${pageContext.request.contextPath}/systemconfig/editdeductionBaseValueEntity.shtml';
                    var data={
                        deductionBaseValue:deductionBaseValue
                    }
                    communatePost(url,ListToJsonString(data),function back(data){
                            showMessagerCenter("恭喜","修改成功");
                    })
            }
        </script>
    </div>
</div>
