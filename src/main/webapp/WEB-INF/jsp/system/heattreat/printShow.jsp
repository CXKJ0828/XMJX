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
<input type="text" id="ids" value="${ids}" style="display: none">
<a href="#"  class="easyui-linkbutton" onclick="printRows()" data-options="iconCls:'printer'">确认打印</a>
<table style="width: 100%" id="contentCode1">
    <tr>
        <td style="text-align:center;"><span style="text-align:center;font-size: 18px;">${companyName}${origin}单</span></td>
    </tr>
    <tr>
        <td>
            <span style="float: left;font-size: 14px;">数量合计:${entity.sumAmount}  总重量合计:${entity.sumWeight}</span>
            <span style="float: right;font-size: 14px;">打印日期:${nowDay}</span></td>
    </tr>
    <tr >
        <td>
            <table id="printRows" border="2" cellspacing="0" style="width: 100%;word-break:keep-all">
                <tr>
                    <td style="font-size:15px;text-align: center">客户</td>
                    <td style="font-size:15px;text-align: center">领料日期</td>
                    <td style="font-size:15px;text-align: center">合同号</td>
                    <td style="font-size:15px;text-align: center">图号</td>
                    <td style="font-size:15px;text-align: center">产品名称</td>
                    <td style="font-size:15px;text-align: center">产品尺寸</td>
                    <td style="font-size:15px;text-align: center">数量</td>
                    <td style="font-size:15px;text-align: center">单重</td>
                    <td style="font-size:15px;text-align: center">总重量</td>
                    <td style="font-size:15px;text-align: center">材质</td>
                    <td style="font-size:15px;text-align: center">硬度</td>
                    <td style="font-size:15px;text-align: center">交货日期</td>
                    <td style="font-size:15px;text-align: center">备注1</td>
                    <td style="font-size:15px;text-align: center">回料日期</td>
                    <td style="font-size:15px;text-align: center">回料数量</td>
                    <td style="font-size:15px;text-align: center">备注2</td>
                </tr>
                <c:forEach items="${rows}" var="entity" varStatus="status">
                <tr>
                    <td style="font-size:15px;text-align: center">${entity.clientName}</td>
                    <td style="font-size:15px;text-align: center">${entity.pickTime}</td>
                    <td style="font-size:15px;text-align: center">${entity.contractNumber}</td>
                    <td style="font-size:15px;text-align: center">${entity.mapNumber}</td>
                    <td style="font-size:15px;text-align: center">${entity.goodName}</td>
                    <td style="font-size:15px;text-align: center">${entity.goodSize}</td>
                    <td style="font-size:15px;text-align: center">${entity.amount}</td>
                    <td style="font-size:15px;text-align: center">${entity.goodWeight}</td>
                    <td style="font-size:15px;text-align: center">${entity.weight}</td>
                    <td style="font-size:15px;text-align: center">${entity.materialQualityName}</td>
                    <td style="font-size:15px;text-align: center">${entity.hardnessName}</td>
                    <td style="font-size:15px;text-align: center">${entity.deliveryTime}</td>
                    <td style="font-size:15px;text-align: center">${entity.remarks1}</td>
                    <td style="font-size:15px;text-align: center">${entity.backTime}</td>
                    <td style="font-size:15px;text-align: center">${entity.backAmount}</td>
                    <td style="font-size:15px;text-align: center">${entity.remarks2}</td>
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
        communateGet(rootPath +'/heattreat/modifyIsPrint.shtml?ids='+getValueById("ids"),function back(data){
            if(data=='success'){
                reloadDatagridMessage('tt');
            }else{
                showErrorAlert("操作",data);
            }
        });
    }
</script>
</body>