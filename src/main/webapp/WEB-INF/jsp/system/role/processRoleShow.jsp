<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<table id="processTable"
       class="easyui-datagrid"
       data-options="rownumbers:true,
           fit:true,
            fitColumns:true,
           url:'${pageContext.request.contextPath}/role/processShow.shtml?roleId='+${roleId}">
    <thead>
    <tr>
        <th data-options="field:'id',hidden:true,width:50,align:'center'">编号</th>
        <th data-options="field:'code',width:200,align:'center',editor:'text'">编码</th>
        <th data-options="field:'name',width:200,align:'center',editor:'text'">名称</th>
        <th data-options="field:'simpleName',width:200,align:'center',editor:'text'">简称</th>
        <th data-options="field:'address',width:200,align:'center',editor:'text'">地址</th>
        <th data-options="field:'remark',width:100,align:'center',editor:'text'">备注</th>
    </tr>
    </thead>
</table>