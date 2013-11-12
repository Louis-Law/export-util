<?xml version="1.0" encoding="UTF-8"?>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>监考人员统计导出</title>
    <style type="text/css">

        body {
            font-family: SimSun;
            font-size : 3.3mm;
        }
        #th{
			font-size:18px;
			background-color:#99CCFF;
         }
        table {

             border-collapse:collapse;
             table-layout: fixed;
        }
      <!-- /*PDF分页所用*/
        div.header-right {
            display: none
        }

        @media print {

            div.header-right {
                display: block;
                position: running(header-right);
            }

            div.footer-center {
                display: block;
                position: running(footer-center);
            }
        }

        @page {
            margin: 0.65in;
            padding: 1em;
            @top-right { content: element(header-right) };
            @bottom-right { content: element(footer-center) };
        }

        #pagenumber:before {
            content: counter(page);
        }

        #pagecount:before {
            content: counter(pages);
        }
         -->

    </style>
</head>
<body>
<!--<div id="header-left" class="header-left" align="left"><img src="http://www.ai9475.com/images/icon_good.gif" width="10" height="10"/>ttt<hr/>打印日期1:</div>
<div id="header-right" class="header-right" align="right">报告对象：<hr/>产生日期1：</div>
-->
 <!--
<div id="footer-left" class="footer-left" align="left"><hr/>Copyright 2000</div>
-->
<!--pdf分页-->
<div id="header-right" class="header-right" align="right">考试时间<hr/></div>
<div id="footer-center" class="footer-center" align="center"><hr/>第 <span id="pagenumber"/> 页/共  <span id="pagecount"/>   页</div>
<table width="200" border="1">
     <tr>
     		<td id="th" width="45" height="12" align="center">考试地点</td>
            <td id="th" width="45" height="12" align="center">考试日期</td>
            <td id="th"  width="45" height="12" align="center">考试时间</td>
            <td id="th"  width="45" height="12" align="center">监考人员</td>
            <td id="th"  width="45" height="12" align="center">监考教室楼</td>
            <td id="th"  width="40" height="12" align="center">监考教室楼层</td>
            <td id="th"  width="45" height="12" align="center">监考教室</td>
     </tr>
    <#list data as dt>
	    <#list dt.JS as d>
	    <tr>
	    	<#if (d_index==0)>
	             <td rowspan="${dt.JSS}" align="center">${dt.DWMC!""}</td>
	             <td rowspan="${dt.JSS}" align="center">${dt.KSRQ!""}</td>
	             <td rowspan="${dt.JSS}" align="center">${dt.KSSJ}</td>
	             <td rowspan="${dt.JSS}" align="center">${dt.XM!""}</td>
	        </#if>
	    	     <td>${d.JSL!""}</td>
	             <td>${d.JSLCH!""}</td>
	             <td>${d.JSH!""}</td>
	    </tr>
	     </#list>
         </#list>
       </table>

</body>
</html>