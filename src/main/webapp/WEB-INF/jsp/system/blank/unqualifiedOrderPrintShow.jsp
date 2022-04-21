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
<table style="width: 100%" id="contentCodeUnqualified">
    <tr>
        <td style="text-align:center;"><span style="text-align:center;font-size: 18px;">产品不合格单</span></td>
    </tr>
    <tr>
        <td><span style="float: right;font-size: 14px;">日期:${nowDay}</span></td>
    </tr>
    <tr>
        <td>
            <table border="1" bordercolor="#a0c6e5" style="border-collapse:collapse;width: 100%">
                <tr style="height: 30px;">
                    <td style="text-align: center">零件名称:</td>
                    <td style="width: 25%">${orderdetails.goodName}</td>
                    <td style="text-align: center">图号:</td>
                    <td style="width: 25%">${orderdetails.mapNumber}</td>
                    <td style="text-align: center">合同号:</td>
                    <td style="width: 25%">${orderdetails.contractNumber}</td>
                </tr>
                <tr style="height: 30px;">
                    <td style="text-align: center">操作者:</td>
                    <td style="width: 25%">${workersubmitFormMap.userName}</td>
                    <td style="text-align: center">检验员:</td>
                    <td style="width: 25%">${userFormMap.userName}</td>
                    <td style="text-align: center">数量:</td>
                    <td style="width: 25%">${workersubmitFormMap.badAmount}</td>
                </tr>
                <tr style="height: 30px;">
                    <td style="text-align: center">不<br>合<br>格<br>原<br>因</td>
                    <td colspan="5">${unqualifiedFormMap.unqualifiedReason}</td>
                </tr>
                <tr style="height: 30px;">
                    <td style="text-align: center">品<br>管<br>主<br>管<br>意<br>见</td>
                    <td colspan="5">${unqualifiedFormMap.directorOpinion}</td>
                </tr>
                <tr style="height: 30px;">
                    <td style="text-align: center">老<br>板<br>审<br>批<br>意<br>见</td>
                    <td colspan="5">${unqualifiedFormMap.bossOpinion}</td>
                </tr>
            </table>
        </td>
    </tr>
</table>
<script>
    function print() {
        $("#contentCodeUnqualified").jqprint({
            debug: false,
            importCSS: true,
            printContainer: true,
            operaSupport: false
        });
    }
</script>
</body>