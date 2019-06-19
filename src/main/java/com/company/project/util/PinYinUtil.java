package com.company.project.util;

import org.springframework.stereotype.Component;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

@Component
public class PinYinUtil {

    private static final String FUXING = "欧阳、太史、端木、上官、司马、东方、独孤、南宫、万俟、闻人、夏侯、诸葛、尉迟、公羊、赫连、澹台、皇甫、宗政、濮阳、公冶、太叔、申屠、公孙、慕容、仲孙、钟离、长孙、宇文、司徒、鲜于、司空、闾丘、子车、亓官、司寇、巫马、公西、颛孙、壤驷、公良、漆雕、乐正、宰父、谷梁、拓跋、夹谷、轩辕、令狐、段干、百里、呼延、东郭、南门、羊舌、微生、公户、公玉、公仪、梁丘、公仲、公上、公门、公山、公坚、左丘、公伯、西门、公祖、第五、公乘、贯丘、公皙、南荣、东里、东宫、仲长、子书、子桑、即墨、淳于、达奚、褚师、吴铭、纳兰、归海";

    public String changeName2PinYin(String name) {

        String familyName = "";
        String personName = "";

        if (FUXING.indexOf(name.substring(0, 1)) > -1) {
            // 是复姓
            familyName = this.change2PinYin(name.substring(0, 2));
            personName = this.change2PinYin(name.substring(2));
        } else {
            familyName = this.change2PinYin(name.substring(0, 1));
            personName = this.change2PinYin(name.substring(1));
        }

        return this.firstUpperCase(familyName) + " " + this.firstUpperCase(personName);
    }

    private String firstUpperCase(String s) {
        return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
    }

    private String change2PinYin(String str) {

        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

        String res = "";
        for (int i = 0; i < str.length(); i++) {
            String[] pinyin;
            try {
                pinyin = PinyinHelper.toHanyuPinyinStringArray(str.charAt(i), defaultFormat);
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                return "";
            }

            res += pinyin[0];
        }
        return res;
    }
}