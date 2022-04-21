<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@include file="/common/common.jspf"%>
<%@include file="/common/print.jspf"%>
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
    .contantMessage{
        font-size: 16px;
    }
</style>
<body>
<input type="text" id="ids" style="display: none" value="${ids}">
<a href="#"  class="easyui-linkbutton" onclick="printRows()" data-options="iconCls:'printer'">确认打印</a>
<table style="width: 100%" id="contentCode1">
    <tr>
        <td style="text-align:center;"><span style="text-align:center;font-size: 18px;">${companyName}送货单</span></td>
    </tr>
    <tr>
        <td>收货单位:${fullName}<span style="float: right;font-size: 14px;">交货日期:${nowDay}</span></td>
    </tr>
    <tr>
        <td>
            <table id="printRows" border="2" cellspacing="0" style="width: 100%;word-break:keep-all">
                <tr>
                    <td style="text-align: center">箱号</td>
                    <td style="text-align: center">订单编号</td>
                    <td style="text-align: center">物料编码</td>
                    <td style="text-align: center">名称</td>
                    <td style="text-align: center">图号</td>
                    <td style="text-align: center">规格尺寸</td>
                    <td style="text-align: center">单位</td>
                    <td style="text-align: center">数量</td>
                    <c:if test="${origin =='price'}">
                        <td style="text-align: center">单价</td>
                        <td style="text-align: center">金额</td>
                    </c:if>

                    <td style="text-align: center">备注</td>
                </tr>
                <c:forEach items="${rows}" var="order" varStatus="status">
                <tr>
                    <td style="text-align: center">${order.boxId}</td>
                    <td style="text-align: center">${order.contractNumber}</td>
                    <td style="text-align: center">${order.materialCode}</td>
                    <td style="text-align: center">${order.goodName}</td>
                    <td style="text-align: center">${order.mapNumber}</td>
                    <td style="text-align: center">${order.goodSize}</td>
                    <td style="text-align: center">件</td>
                    <td style="text-align: center">${order.sendAmount}</td>
                    <c:if test="${origin =='price'}">
                        <td style="text-align: center">${order.taxPrice}</td>
                        <td style="text-align: center"><fmt:formatNumber type="number" value="${order.sendAmount*order.taxPrice}" pattern="0.00" maxFractionDigits="2"/></td>
                    </c:if>
                    <td style="text-align: center">${order.sendRemarks}</td>
                </tr>
                </c:forEach>
                <tr>
                    <td style="text-align: center">共${boxCount}箱</td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <c:if test="${origin =='price'}">
                        <td></td>
                        <td></td>
                    </c:if>
                    <td></td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td style="text-align: center">收料员/仓管员签收：</td>
    </tr>
    <tr>
        <td>注：此单一式三联 白联：仓管部； 粉联：供应部； 黄联：供应商</td>
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