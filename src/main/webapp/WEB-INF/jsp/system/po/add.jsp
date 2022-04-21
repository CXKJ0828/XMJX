<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<style>
    .tableinput{
        width:100%;
        padding: 5px
    }
</style>
<script type="text/javascript" src="${ctx}/js/system/po/add.js"></script>
<div style="width:100%;">
    <form id="ff"  method="post">
            <table  class="tableinput">
                <tr>
                    <td >编码:</td>
                    <td>
                        <input class="easyui-validatebox" type="text" name="poFormMap.code"   placeholder="请输入编码"></input>
                    </td>
                    <td >客户名称:</td>
                    <td>
                        <input class="easyui-validatebox" type="text" name="poFormMap.clientName"   placeholder="请输入客户名称"></input>
                    </td>
                    <td >PO号:</td>
                    <td><input class="easyui-validatebox" type="text" name="poFormMap.po"   placeholder="请输入PO号"></input></td>

                </tr>
                <tr>
                    <td >客户型号:</td>
                    <td><input class="easyui-validatebox" type="text" name="poFormMap.clientCode"   placeholder="请输入客户型号"></input></td>
                    <td>CX型号:</td><td><input class="easyui-validatebox" type="text" name="poFormMap.cx"    placeholder="请输入CX型号"></input></td>
                    <td>备注:</td><td><input class="easyui-validatebox" type="text" name="poFormMap.remark"   placeholder="请输入备注"></input></td>
                </tr>
            </table>
        <%@include file="/line.jsp"%>
        <table  class="tableinput">
            <tr>
                <td >包装尺寸/CM</td>
                <td>长</td>
                <td>
                    <input class="easyui-validatebox" name="poFormMap.lengthC" type="text" ></input>
                </td>
                <td>宽</td>
                <td>
                    <input class="easyui-validatebox" name="poFormMap.widthC" type="text" ></input>
                </td>
                <td>高</td>
                <td>
                    <input class="easyui-validatebox" name="poFormMap.heightC" type="text" ></input>
                </td>
            </tr>
        </table>
        <%@include file="/line.jsp"%>
        <table  class="tableinput">
            <tr>
                <td >&nbsp;&nbsp;包装尺寸/IN</td>
                <td>长</td>
                <td>
                    <input class="easyui-validatebox" name="poFormMap.lengthI" type="text" ></input>
                </td>
                <td>宽</td>
                <td>
                    <input class="easyui-validatebox" name="poFormMap.widthI" type="text" ></input>
                </td>
                <td>高</td>
                <td>
                    <input class="easyui-validatebox" name="poFormMap.heightI" type="text" ></input>
                </td>
            </tr>
        </table>
        <%@include file="/line.jsp"%>
        <table  class="tableinput">
            <tr>
                <td>立方/CBM:</td>
                <td>
                    <input class="easyui-validatebox" name="poFormMap.cube" type="text" ></input>
                </td>
                <td>N.W/LBS</td>
                <td>
                    <input class="easyui-validatebox" name="poFormMap.nw" type="text" ></input>
                </td>
                <td>净重/KG:</td>
                <td>
                    <input class="easyui-validatebox" name="poFormMap.netWeight" type="text" ></input>
                </td>
            </tr>
            <tr>
                <td>G.W/LBS</td>
                <td>
                    <input class="easyui-validatebox" name="poFormMap.gw" type="text" ></input>
                </td>
                <td>毛重/KG:</td>
                <td>
                    <input class="easyui-validatebox" name="poFormMap.grossWeight" type="text" ></input>
                </td>
            </tr>
        </table>
    </form>
</div>
<div style="text-align:center;padding:5px">
    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm()">确定</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm()">重置</a>
</div>