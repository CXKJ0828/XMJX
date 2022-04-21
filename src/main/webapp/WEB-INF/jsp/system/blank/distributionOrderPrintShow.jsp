<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<%@include file="/common/print.jspf"%>
<style>
    .tdshow{
        font-size: 16px
    }
</style>
<body>
<a href="#"  class="easyui-linkbutton" onclick="printRows()" data-options="iconCls:'printer'">确认打印</a>
<table style="width: 100%" id="contentCode1">
    <tr>
        <td><img style="float: right;margin-right: 10px"  src="data:image/png;base64,${img}">
        </td>
    </tr>
    <tr>
        <td style="text-align:center;"><span style="text-align:center;font-size: 18px;">${companyName}派工单</span></td>
    </tr>
    <tr>
        <td>
            <span style="float: left;font-size: 14px;">接收人:${userName}</span>
            <span style="float: right;font-size: 14px;">打印日期:${nowDay}</span></td>
    </tr>
    <tr >
        <td>
            <table id="printRows" border="2" cellspacing="0" style="width: 100%;word-break:keep-all">
                <tr>
                    <td style="font-size:15px;text-align: center">编号</td>
                    <td style="font-size:15px;text-align: center">交货日期</td>
                    <td style="font-size:15px;text-align: center">客户</td>
                    <td style="font-size:15px;text-align: center">订单号</td>
                    <td style="font-size:15px;text-align: center">图号</td>
                    <td style="font-size:15px;text-align: center">产品名称</td>
                    <td style="font-size:15px;text-align: center">材质</td>
                    <td style="font-size:15px;text-align: center">工序号</td>
                    <td style="font-size:15px;text-align: center">工序</td>
                    <td style="font-size:15px;text-align: center">接收数量</td>
                </tr>
                <c:forEach items="${rows}" var="order" varStatus="status">
                <tr>
                    <td style="font-size:15px;text-align: center">${order.code}</td>
                    <td style="font-size:15px;text-align: center">${order.deliveryTime}</td>
                    <td style="font-size:15px;text-align: center">${order.fullName}</td>
                    <td style="font-size:15px;text-align: center">${order.contractNumber}</td>
                    <td style="font-size:15px;text-align: center">${order.mapNumber}</td>
                    <td style="font-size:15px;text-align: center">${order.goodName}</td>
                    <td style="font-size:15px;text-align: center">${order.materialQualityName}</td>
                    <td style="font-size:15px;text-align: center">${order.number}</td>
                    <td style="font-size:15px;text-align: center">${order.processName}</td>
                    <td style="font-size:15px;text-align: center">${order.nowreceiveAmount}</td>
                    </c:forEach>
            </table>
        </td>
    </tr>
</table>
<script>
    function printRows() {
        $("#contentCode1").jqprint({
            debug: false,
            importCSS: true,
            printContainer: true,
            operaSupport: false
        });
       
    }
</script>
</body>