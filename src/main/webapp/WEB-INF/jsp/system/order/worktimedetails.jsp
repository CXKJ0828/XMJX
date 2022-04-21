<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<style>
    .tdShow{
        width:20%;
        height: 40px;
        text-align: left;
    }
    .spanShow{
        width: 30%!important;
    }
    .inputShow{
        width: 70%;
    }
</style>
<body class="easyui-layout">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/order/details.js"></script>
<div class="easyui-layout" data-options="fit:true">
<div data-options="region:'north',split:true" >
        <table border="1"  cellspacing="5" style="border-color:gainsboro">
            <tbody>
            <tr>
                <td class="tdShow"><span class="spanShow">单号：</span>
                    <input id="id" style="display: none" type="text" class="inputShow easyui-validatebox"  value="${order.id}">
                    <input id="orderId" type="text" class="inputShow easyui-validatebox"  value="${order.orderId}"> </td>
                <td class="tdShow"><span class="spanShow">日期：</span>
                    <input id="time" type="text" value="${order.time}"></td>
                <td class="tdShow"><span class="spanShow">产品名称：</span><input id="name" type="text" class="inputShow easyui-validatebox"  value="${order.name}"> </td>
                <td class="tdShow"><span class="spanShow">数量：</span><input id="amount" type="text" class="inputShow easyui-validatebox"  value="${order.amount}"> </td>
            </tr>
            <script>
                initInputDataInput("time");
            </script>
            <tr>
                <td class="tdShow"><span class="spanShow">制表时间：</span><input id="modifyTime" type="text" class="inputShow easyui-validatebox"  value="${order.modifyTime}"> </td>
                <td class="tdShow"><span class="spanShow">制表用户：</span><input id="modifyUserId" type="text" class="inputShow easyui-validatebox"  value="${order.userName}"> </td>
                <td class="tdShow" ><span class="spanShow">单据状态：</span><input id="state" type="text" class="inputShow easyui-validatebox"  value="${order.state}"> </td>
                <td class="tdShow" ><span class="spanShow">备注：</span><input id="remarks" type="text" class="inputShow easyui-validatebox"  value="${order.remarks}"> </td>
            </tr>
            </tbody>
        </table>
</div>
<div data-options="region:'center'" style="height:auto;">
    <table id="orderdetails"
           title="订单明细"
           class="easyui-datagrid"
           data-options="rownumbers:true,
           fit:true,
            view:detailview,
            detailFormatter:detailFormatter,
            onExpandRow:onExpandRow,
            fitColumns:true,
            singleSelect:true,
            onDblClickRow:printEntity,
           url:'${pageContext.request.contextPath}/order/getOrderDetailsByOrderId.shtml?id='+${order.id}">
        <thead>
        <tr>
            <th data-options="field:'id',hidden:true,width:50,align:'center'">编号</th>
            <th data-options="field:'batchNumber',width:200,align:'center',editor:'text'">批号</th>
            <th data-options="field:'goodId',width:200,align:'center',editor:'text'">件号</th>
            <th data-options="field:'goodName',width:200,align:'center',editor:'text'">件名</th>
            <th data-options="field:'amount',width:200,align:'center',editor:'text'">订单数量</th>
            <th data-options="field:'state',width:100,align:'center',editor:'text'">状态</th>
        </tr>
        </thead>
    </table>
</div>
</div>
<div id="codeShow">

</div>
<%@include file="/rightOpration.jsp"%>
<script>
    initComboGridEditor();
    function printEntity(rowIndex, rowData){
        var code=rowData.batchNumber;
        $("#codeShow").window({
            width:400,
            title:'二维码展示',
            modal: true,
            height: 350,
            top:50,
            href: rootPath + '/order/codeUI.shtml?code='+code,
            onClose:function () {

            }
        });
    }
</script>

</body>