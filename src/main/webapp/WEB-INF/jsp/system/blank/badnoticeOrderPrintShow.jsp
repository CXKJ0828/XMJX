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
<table style="width: 100%" id="contentCode">
    <tr>
        <td style="text-align:center;"><span style="text-align:center;font-size: 18px;">废品通知单</span></td>
    </tr>
    <tr>
        <td>姓名:${workersubmitFormMap.userName}<span style="float: right;font-size: 14px;">日期:${nowDay}</span></td>
    </tr>
    <tr>
        <td>
            <table border="1" bordercolor="#a0c6e5" style="border-collapse:collapse;width: 100%">
                <tr style="height: 30px;">
                    <td style="text-align: center">名称</td>
                    <td style="text-align: center">合同号</td>
                    <td style="text-align: center">图号</td>
                    <td style="text-align: center">数量</td>
                    <td style="text-align: center">图纸尺寸</td>
                    <td style="text-align: center">实际尺寸</td>
                    <td style="text-align: center">判定结果</td>
                </tr>
                <tr style="height: 30px;">
                    <td style="text-align: center">${orderdetails.goodName}</td>
                    <td style="text-align: center">${orderdetails.contractNumber}</td>
                    <td style="text-align: center">${orderdetails.mapNumber}</td>
                    <td style="text-align: center">${workersubmitFormMap.badAmount}</td>
                    <td style="text-align: center">${orderdetails.goodSize}</td>
                    <td style="text-align: center">${badnoticeFormMap.trueSize}</td>
                    <td style="text-align: center">${badnoticeFormMap.result}</td>
                </tr>
                <tr style="height: 30px;">
                    <td style="text-align: center">备注</td>
                    <td colspan="6">${badnoticeFormMap.remarks}</td>
                </tr>
            </table>
        </td>
    </tr>
</table>
<script>
    function print() {
        $("#contentCode").jqprint({
            debug: false,
            importCSS: true,
            printContainer: true,
            operaSupport: false
        });
    }
</script>
</body>