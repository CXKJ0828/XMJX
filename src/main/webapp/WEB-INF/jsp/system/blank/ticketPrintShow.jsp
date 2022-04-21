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
<input type="text" id="ids" style="display: none" value="${ids}">
<a href="#"  class="easyui-linkbutton" onclick="printRows()" data-options="iconCls:'printer'">确认打印</a>
<div id="printRows" style="margin: 0px;width:210mm;">
    <c:forEach items="${rows}" var="listEntity" varStatus="status">
       <table  border="1mm" cellspacing="0" style="width:210mm;height: 297mm;">
        <c:set value="${listEntity.list}" var="subItem"/>
        <c:forEach items="${subItem}" var="order">
            <tr style="height:49.5mm">
                <td style="font-size:15px;text-align: center;width: 33mm;">${order.clientFullName}(${order.code})</td>
                <td style="font-size:15px;text-align: center;width: 20mm;">${order.contractNumber}</td>
                <td style="font-size:15px;text-align: center;width: 20mm;">${order.goodName}</td>
                <td style="font-size:15px;text-align: center;width: 20mm;">${order.mapNumber}</td>
                <td style="font-size:15px;text-align: center;width: 20mm;">${order.blankSize}</td>
                <td style="font-size:15px;text-align: center;width: 14mm;">${order.amount}</td>
                <td style="font-size:15px;text-align: center;width: 23mm;">${order.deliveryTime}</td>
                <td style="font-size:15px;text-align: center;width: 20mm;">${order.materialQualityName}</td>
                <td style="font-size:15px;text-align: center;width: 20mm">${order.remarks1}</td>
                <td style="font-size:15px;text-align: center;width: 20mm;">${order.remarks2}</td>
            </tr>
        </c:forEach>
    </table>
        <div style="page-break-after:always;"></div>
    </c:forEach>
</div>

<script>
    function printRows() {
        $("#printRows").jqprint({
            debug: true,
            importCSS: true,
            printContainer: true,
            operaSupport: false
        });

    }
</script>
</body>