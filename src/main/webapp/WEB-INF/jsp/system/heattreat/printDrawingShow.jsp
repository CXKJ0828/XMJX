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
<div id="contentCodeDrawing"  style="margin: 0px;width:297mm;">
    <c:forEach items="${rows}" var="entity" varStatus="status">
        <table style="width:100%;height:100%" >
            <tr>
                <td style="font-size: 20px">
                    合同号:${entity.contractNumber}
                </td>
                <td style="font-size: 20px">
                    交期:${entity.deliveryTime}
                </td>
                <td style="font-size: 20px">
                    图号:${entity.mapNumber}
                </td>
                <td style="font-size: 20px">
                    名称:${entity.name}
                </td>
            </tr>
            <tr style="height:190mm;width:100%;">
                <td colspan="4">
                    <img  style="align-content: center;width: 290mm;height:190mm" src="${entity.img}" />
                </td>
            </tr>
        </table>
            <div style="page-break-after:always;"></div>
    </c:forEach>
</div>
<script>
    function printRows() {
        $("#contentCodeDrawing").jqprint({
            debug: false,
            importCSS: true,
            printContainer: true,
            operaSupport: false
        });
    }
</script>
</body>