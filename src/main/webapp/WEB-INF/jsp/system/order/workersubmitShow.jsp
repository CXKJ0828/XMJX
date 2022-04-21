<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<table id="processTable"
       class="easyui-datagrid"
       data-options="rownumbers:true,
           fit:true,
            fitColumns:true,
           url:'${pageContext.request.contextPath}/order/workersubmitShow.shtml?orderdetailsprocessId='+${orderdetailsprocessId}">
    <thead>
    <tr>
        <th data-options="field:'id',hidden:true,width:50,align:'center'">编号</th>
        <th data-options="field:'submitAccountName',width:200,align:'center',editor:'text'">操作者编码</th>
        <th data-options="field:'submitUserName',width:200,align:'center',editor:'text'">操作者名称</th>
        <th data-options="field:'submitTime',width:200,align:'center',editor:'text'">完成时间</th>
        <th data-options="field:'amount',width:200,align:'center',editor:'text'">提交数量</th>
        <th data-options="field:'usetime',width:100,align:'center',editor:'text'">耗用工时</th>
    </tr>
    </thead>
</table>