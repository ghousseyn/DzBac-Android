package com.squalala.dzbac.utils;

import android.util.Log;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : StringUtils.java
 * Date : 22 juil. 2014
 * 
 */
public class StringUtils {
	
	public static String replaceCharAt(String s, int pos, String c) {
	    return s.substring(0, pos) + c + s.substring(pos + 1);
	}

    public static String toEmoji(String text) {
      /*  System.out.println("(StringEscapeUtils.unescapeJava  : " + (StringEscapeUtils.unescapeJava(text)));
        System.out.println("StringEscapeUtils.unescapeJava(StringEscapeUtils.unescapeJava : "
                + EmojiParser.parseToUnicode(StringEscapeUtils.unescapeJava(StringEscapeUtils.unescapeJava(text))));*/
       // return EmojiParser.parseToUnicode(text);
        try {
            return StringEscapeUtils.unescapeJava(StringEscapeUtils.unescapeJava(text));
        } catch (NestableRuntimeException e) {
            return text;
        }
    }


    public static String escapeString(String str) {

   /*     System.out.println("original  : " + str);
        System.out.println("parseToUnicode : " + EmojiParser.parseToUnicode(str));
        System.out.println("parseToHtmlDecimal : " + EmojiParser.parseToHtmlDecimal(str));
        System.out.println("parseToAliases : " + EmojiParser.parseToAliases(str, EmojiParser.FitzpatrickAction.IGNORE));
        System.out.println("parseToAliases : " + EmojiParser.parseToAliases(str, EmojiParser.FitzpatrickAction.REMOVE));
        System.out.println("parseToAliases : " + EmojiParser.parseToAliases(str, EmojiParser.FitzpatrickAction.PARSE));


        System.out.println("Apache : " +StringEscapeUtils.escapeJava(str));
        System.out.println("Apache : " +StringEscapeUtils.escapeHtml(str));
        System.out.println("Escape : " + escapeJavaString(str));
        System.out.println("test " + str.replaceAll("\"", "\\\\\""));*/
        return escapeJavaString(str);
      //  return EmojiParser.parseToAliases(str);
    }


    public static String escapeJavaString(String st){

        return st;

      /*  StringBuilder builder = new StringBuilder();
        try {
            for (int i = 0; i < st.length(); i++) {
                char c = st.charAt(i);
                if(!Character.isLetterOrDigit(c) && !Character.isSpaceChar(c)&& !Character.isWhitespace(c) ){
                    String unicode = String.valueOf(c);
                    int code = (int)c;
                    System.out.println("Unicode : " + unicode);
                    System.out.println("code : " + code);

                    if (!((code == 1567 && unicode.equals("؟"))
                            ||
                            (code == 1548 && unicode.equals("،"))
                            ||
                            (code == 1563 && unicode.equals("؛")))) {
                        if(!(code >= 0 && code <= 255)){
                            unicode = "\\\\u"+Integer.toHexString(c);
                        }

                    }

                    builder.append(unicode);
                }
                else{
                    builder.append(c);
                }
            }
            Log.i("Unicode Block", builder.toString());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return builder.toString(); */
    }

}
