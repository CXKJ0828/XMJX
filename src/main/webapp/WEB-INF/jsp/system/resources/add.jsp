<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript" src="${ctx}/js/system/resources/add.js"></script>
<div style="padding:10px 0 10px 60px">
    <form id="ff"  method="post">
        <table>
            <tr>
                <td>菜单名称:</td>
                <td><input class="easyui-validatebox" type="text" name="resFormMap.name" data-options="required:true" placeholder="请输入菜单名称"></input></td>
            </tr>
            <tr>
                <td>菜单标识:</td>
                <td><input class="easyui-validatebox" type="text" name="resFormMap.resKey" data-options="required:true"  placeholder="请输入菜单标识"></input></td>
            </tr>
            <tr>
                <td>是否隐藏:</td>
                <td>
                    <input class="easyui-validatebox" type="checkbox" name="resFormMap.ishide" data-options="required:true"  placeholder="请输入菜单标识"></input>
                </td>
            </tr>
            <tr>
                <td>菜单URL:</td>
                <td><input class="easyui-validatebox" type="text" name="resFormMap.resUrl" data-options="required:true" placeholder="请输入菜单URL"></input></td>
            </tr>
            <tr>
                <td>上级菜单:</td>
                <td>
                    <select  id="parentId" name="resFormMap.parentId">
                    </select>
                </td>
            </tr>
            <tr>
                <td>菜单类型:</td>
                <td>
                    <select  onchange="but(this)" name="resFormMap.type">
                        <option value="0">------  目录  ------</option>
                        <option value="1">------  菜单  ------</option>
                        <option value="2">------  按扭  ------</option>
                    </select>
                </td>
            </tr>
            <tr id="divbut" style="display: none;">
                <td>选择:</td>
                <td>
                    <div>
                        <c:forEach items="${listbutton}" var="key">
                            <span onclick="toBut(this)" id='${key.id}'>${key.buttom}</span>
                        </c:forEach>
                    </div>
                    <font color="red">可自定义填入html代码</font>
                </td>
            </tr>
            <tr>
                <td>菜单描述:</td>
                <td><textarea name="resFormMap.description" id="description" style="height:60px;" placeholder="请输入菜单描述"></textarea></td>
            </tr>
        </table>
    </form>
</div>
<div style="text-align:center;padding:5px">
    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm()">确定</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm()">重置</a>
</div>