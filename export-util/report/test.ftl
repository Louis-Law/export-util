<?xml version="1.0" encoding="UTF-8"?>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>${dwmc}</title>
    <style type="text/css">

        body {
            font-family: SimSun;
        }
         #xx{
        background-color:#0000ff;

         }
        table {

             border-collapse:collapse;
             table-layout: fixed;
        }
        
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
<div id="header-right" class="header-right" align="right">${dwmc}<hr/></div>
<div id="footer-center" class="footer-center" align="center"><hr/>第 <span id="pagenumber"/> 页/共  <span id="pagecount"/>   页</div>
	<h3>${kcmc}</h3>
	<#list sts as st> 
		<table style="border:none;margin-bottom:10px;">
				<thead><th align="left">${st.STLB!""}</th></thead>
				<#list st.s as s>
				<tr><td>${s!""}</td></tr>
				</#list>
		</table>
	</#list>
</body>
</html>