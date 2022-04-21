<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@include file="/common/common.jspf"%>
<%@include file="/common/print.jspf"%>
<style>
</style>
<body>
<a href="#"  class="easyui-linkbutton" onclick="printRows()" data-options="iconCls:'printer'">确认打印</a>
<table  style="width: 100%" border="1" cellspacing="0"  id="contentCode1">
    <tr >
        <td colspan="4" style="text-align: center;font-size: 18px;">
            工资条
            <br>
            <span  style="float:right;width: 100%;text-align: right">${time}</span>
        </td>
    </tr>
    <tr >
        <td style="text-align: center;font-size: 16px">${user}</td>
        <td style="text-align: center">应得工资<br>合计:${money.wages}</td>
        <td style="text-align: center">废品应扣工资<br>合计:${money.deductWages}</td>
        <td style="text-align: center">实得工资<br>合计:${money.trueWages}</td>
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