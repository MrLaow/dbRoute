package org.janson.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	


	public static boolean isEmpty(String str) {
		return str == null || "".equals(str.trim());
	}

	public static boolean isNotEmpty(String str) {
		return str != null && str.trim().length() > 0;
	}

	public static boolean isLongNumber(String str) {
		try {
			Long.parseLong(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
	
	public static boolean isNumber(String str){
		try {
			Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public static String replace(String str, String[] args) {
		String result = str;
		for (int i = 0; i < args.length; i++) {
			result = result.replaceAll("\\{" + i + "\\}", args[i]);
		}

		return result;
	}

	public static boolean isContains(String str, String[] args) {
		boolean isContain = false;
		for (String string : args) {
			if (string.equals(str)) {
				isContain = true;
				break;
			}
		}
		return isContain;
	}
	
    /**
     * 对象为NULL时，转换为空
     *
     * @param obj 验证对象
     * @return 空对象
     */
    public static String convertEmptyWhenNull(String obj) {
        if (obj == null) {
            return "";
        } else {
            return obj.toString();
        }
    }

    /**
     * 验收数字是否为正数
     *
     * @param str 验证对象
     * @return 正数:true,其他:false
     */
    public static boolean isPositiveNum(String str) {
        Pattern pattern = null;
        if (str == null) {
            return false;
        }
        pattern = Pattern.compile("^\\d+$");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 验证两个对象是否相等
     *
     * @param obj1 对象一
     * @param obj2 对象二
     * @return 相等 true，不相等：false
     */
    public static boolean isEqual(Object obj1, Object obj2) {
        if (obj1 == null && obj2 == null) {
            return true;
        } else if (obj1 == null) {
            return false;
        } else if (obj2 == null) {
            return false;
        } else {
            return obj1.equals(obj2);
        }
    }

    /**
     * 验证对象是否为空或NULl
     *
     * @param str 验证对象
     * @return 处理结果 空/Null:true,否则:false
     */
    public static boolean isEmptyOrNull(Object str) {
        if ("".equals(str) || null == str) {
            return true;
        }
        return false;
    }

    /**
     * 验证字符串是否为空字符串或NULl
     *
     * @param str 验证对象
     * @return 处理结果 空/Null:true,否则:false
     */
    public static boolean isSpaceOrNull(String str) {
        if (null == str || "".equals(str.trim())) {
            return true;
        }
        return false;
    }

    /**
     * 验证对象不为空也不为NULL
     *
     * @param str 验证对象
     * @return 处理结果 空/Null:true,否则:false
     */
    public static boolean isNotEmptyOrNull(Object str) {
        if (!"".equals(str) && null != str) {
            return true;
        }
        return false;
    }

    /**
     * 验证对象是否为null
     *
     * @param str 验证对象
     * @return 处理结果 null:true,否则:false
     */
    public static boolean isNull(Object str) {
        if ("".equals(str) || null == str) {
            return true;
        }
        return false;
    }

    /**
     * 验证字符串为数据
     *
     * @param str 字符串
     * @return 数字：true，其他：false
     */
    public static boolean isNumeric(String str) {
        if (isSpaceOrNull(str)) {
            return false;
        }
        Pattern pattern = null;
        pattern = Pattern.compile("^-?\\d+$");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 自定义正则表达式进行各类验证
     *
     * @param str   待验证字符串
     * @param regex
     * @return 验证通过：true, 不通过：false
     */
    public static boolean regularValid(String str, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        if (!m.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 将字符串分解
     *
     * @param str      字符串
     * @param separate 分隔符
     * @return 分隔数组
     */
    public static String[] splitToArr(String str, String separate) {
        if (isEmptyOrNull(str)) {
            return new String[0];
        } else {
            return str.split(separate);
        }
    }
    
    public static String lpending(String str, int toLength, String pendingChar){
    	if(str.length()<toLength){
    		String prefix = "";
    		for(int i=str.length();i<toLength;i++){
    			prefix+=pendingChar;
    		}
    		str = prefix+str;
    	}
    	
    	return str;
    }


    public static void main(String[] args) {
		String a = "30011879095014";
		String b = "2"+lpending(a, 19, "0");
		System.out.println(b.length()+"   "+b);
	}
}
