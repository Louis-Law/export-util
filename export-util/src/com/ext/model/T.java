package com.ext.model;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * <dl>
 * <dt>
 * <dd>描述: </dd>
 * <dd>公司: 青牛（北京）技术有限公司</dd>
 * <dd>创建时间：2010-7-16  14:58:44</dd>
 * <dd>创建人： mixiang</dd>
 * </dl>
 */
public class T {

    public static void main(String [] args){
            // String reg = "(.*)\\{(.*)\\}";
            String style = " body {" +
                    "            font-family: SimSun;" +
                    "        }" +
                    "         #xx{" +
                    "        background-color:#0000ff;" +
                    "         }";
          // String styleContent = getPatternValue(style,reg, 1);
            System.out.println(style.replace("}","},"));
    }
     //通过正则表达式获得匹配的值
      public static String getPatternValue(String src, String pattern, int p) {
            String res = null;
            // 创建一个模式
            Pattern pat = Pattern.compile(pattern, Pattern.MULTILINE
                    | Pattern.CASE_INSENSITIVE);
            // 根据创建的模式建立一个配置器
            Matcher matcher = pat.matcher(src.replace("\n",""));
            // 根据配置器匹配到类容
            if (matcher.find()) {
                res = matcher.group(2);
               //res =  matcher.group() ;
            }
            if(res!=null)
            {
                 return res.trim();
            }
              return res;
     }
    
}
