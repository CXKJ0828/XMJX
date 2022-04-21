<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
<a href="#"  class="easyui-linkbutton" onclick="print()" data-options="iconCls:'printer'">确认打印</a>
<table style="width: 100%" id="contentCode1">
    <tr>
        <td style="text-align:center;"><span style="text-align:center;font-size: 18px;">外协废品单</span></td>
    </tr>
    <tr>
        <td><span style="float: right;font-size: 14px;">日期:${nowDay}</span></td>
    </tr>
    <tr>
        <td>
            <table border="1" bordercolor="#a0c6e5" style="border-collapse:collapse;width: 100%">
                <tr style="height: 30px;">
                    <td style="text-align: center">合同号:</td>
                    <td style="width: 25%">${scrapFormMap.contractNumber}</td>
                    <td style="text-align: center">图号:</td>
                    <td style="width: 25%">${scrapFormMap.mapNumber}</td>
                    <td style="text-align: center">零件名称:</td>
                    <td style="width: 25%">${scrapFormMap.goodName}</td>
                </tr>
                <tr style="height: 30px;">
                    <td style="text-align: center">工序名称:</td>
                    <td style="width: 25%">${scrapFormMap.processName}</td>
                    <td style="text-align: center">数量:</td>
                    <td style="width: 25%">${scrapFormMap.badAmount}</td>
                    <td style="text-align: center">操作者:</td>
                    <td style="width: 25%">${scrapFormMap.userName}</td>
                </tr>
                <tr style="height: 30px;">
                    <td style="text-align: center">废品原因:</td>
                    <td colspan="3">${scrapFormMap.badReason}</td>
                    <td style="text-align: center">材料:</td>
                    <td style="width: 25%">${scrapFormMap.materialquality}</td>
                </tr>
                <tr style="height: 30px;">
                    <td style="text-align: center" colspan="6">扣款明细</td>
                </tr>
                <tr style="height: 30px;">
                    <td style="text-align: center">材料费:</td>
                    <td colspan="5">${scrapFormMap.materialCost}</td>
                </tr>
                <tr style="height: 30px;">
                    <td style="text-align: center">锻造费:</td>
                    <td colspan="5">${scrapFormMap.forgingCost}</td>
                </tr>
                <tr style="height: 30px;">
                    <td style="text-align: center">调质费:</td>
                    <td colspan="5">${scrapFormMap.adjustmentCost}</td>
                </tr>
                <tr style="height: 30px;">
                    <td style="text-align: center">下料费:</td>
                    <td colspan="5">${scrapFormMap.cuttingCost}</td>
                </tr>
                <tr style="height: 30px;">
                    <td style="text-align: center">车工费:</td>
                    <td colspan="5">${scrapFormMap.carriageCost}</td>
                </tr>
                <tr style="height: 30px;">
                    <td style="text-align: center">合计:</td>
                    <td colspan="5">${scrapFormMap.allCost}</td>
                </tr>
                <tr style="height: 30px;">
                    <td style="text-align: center">检验结论:</td>
                    <td colspan="3">${scrapFormMap.conclusion}</td>
                    <td style="text-align: center">检验:</td>
                    <td style="width: 25%">${userFormMap.userName}</td>
                </tr>
                <tr style="height: 30px;">
                    <td style="text-align: center">批准人:</td>
                    <td colspan="5">${scrapFormMap.approvalName}</td>
                </tr>
            </table>
        </td>
    </tr>
</table>
<script>
    function print() {
        $("#contentCode1").jqprint({
            debug: false,
            importCSS: true,
            printContainer: true,
            operaSupport: false
        });
    }
</script>
</body>