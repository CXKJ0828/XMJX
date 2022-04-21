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
<div id="contentCodeTechnology"  style="margin: 0px;width:100%;">
    <c:forEach items="${rows}" var="entity" varStatus="status">
        <table style="width:100%;" >
            <tr>
                <td colspan="5" style="font-size: 24px;text-align: center">工艺卡</td>
            </tr>
            <tr >
                <td style="font-size: 20px">
                    合同号:${entity.contractNumber}
                </td>
                <td style="font-size: 20px">
                    交期:${entity.deliveryTime}
                </td>
                <td style="font-size: 20px">
                    数量:${entity.amount}
                </td>
                <td style="font-size: 20px">
                    图号:${entity.mapNumber}
                </td>
                <td style="font-size: 20px">
                    名称:${entity.name}
                </td>
            </tr>
            <tr style="width:100%;">
                <td colspan="5">
                    <table id="printRows" border="1" cellspacing="0" style="width: 100%;word-break:keep-all">
                        <tr>
                            <td style="font-size:15px;text-align: center">步骤</td>
                            <td style="font-size:15px;text-align: center">工序</td>
                            <td style="font-size:15px;text-align: center">加工内容</td>
                        </tr>
                        <c:forEach items="${entity.process}" var="processEntity" varStatus="status">
                        <tr>
                            <td style="font-size:15px;text-align: center">${processEntity.number}</td>
                            <td style="font-size:15px;text-align: center">${processEntity.processName}</td>
                            <td style="font-size:15px;text-align: center">${processEntity.content}</td>
                            </c:forEach>
                    </table>
                </td>
            </tr>
        </table>
        <div style="page-break-after:always;"></div>
    </c:forEach>
</div>

<script>
    function printRows() {
        $("#contentCodeTechnology").jqprint({
            debug: false,
            importCSS: true,
            printContainer: true,
            operaSupport: false
        });
    }
</script>
</body>