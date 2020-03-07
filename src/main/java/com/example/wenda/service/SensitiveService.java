package com.example.wenda.service;


import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 敏感词查找
 */
@Service
public class SensitiveService implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveService.class);


    @Override
    public void afterPropertiesSet() throws Exception {

        try {

            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
            InputStreamReader reader = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(reader);

            String lineTxt;
            while ((lineTxt = bufferedReader.readLine()) != null){
                lineTxt = lineTxt.trim();
                addWord(lineTxt);
            }

        }catch (Exception e){
            logger.error("读取敏感词文件失败" + e.getMessage());
        }
    }

    //增加关键词
    private void addWord(String lineText){
        TreeNode tempNode = rootNode;

        for(int i = 0;i< lineText.length(); ++i){
            Character c = lineText.charAt(i);

            TreeNode node = tempNode.getSubNode(c);

            if(node == null){
                node = new TreeNode();
                tempNode.addSubNode(c,node);
            }

            tempNode = node;

            if(i == lineText.length() - 1){
                tempNode.setKeyWordEnd(true);
            }
        }
    }

    /* 前缀树节点构建 */
    private class TreeNode{

        //是否已到达关键词的结尾
        private boolean end = false;

        private Map<Character,TreeNode> subNodes = new HashMap<>();

        public void addSubNode(Character key, TreeNode node){
            subNodes.put(key,node);
        }

        TreeNode getSubNode(Character key){
            return subNodes.get(key);
        }

        boolean isKeyWordEnd(){
            return end;
        }

        void setKeyWordEnd(boolean end){
            this.end = end;
        }
    }

    private TreeNode rootNode = new TreeNode();

    public String filter(String text){
        if (StringUtils.isBlank(text)) {

            return text;
        }

        String replacement = "***";
        StringBuilder result = new StringBuilder();

        TreeNode tempNode = rootNode;
        int begin = 0;
        int position = 0;

        while(position < text.length()){
            char c = text.charAt(position);

            //空格直接跳过
            if (isSymbol(c)) {
                //在头部，直接丢掉改内容
                if(tempNode == rootNode){
                    result.append(c);
                    ++begin;
                }

                ++ position;
                continue;
            }

            tempNode = tempNode.getSubNode(c);

            if(tempNode == null){
                result.append(text.charAt(begin));
                position = begin + 1;
                begin = position;
                tempNode = rootNode;

            }else if (tempNode.isKeyWordEnd()){
                result.append(replacement);
                position = position + 1;
                begin = position;
                tempNode = rootNode;
            }
            else {
                 ++ position;
            }
        }

        result.append(text.substring(begin));
        return result.toString();
    }

    private boolean isSymbol(char c){
        int s = (int) c;
        // 0x2E80-0x9FFF 东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (s < 0x2E80 || s > 0x9FFF);
    }

    public static void main(String[] args) {
        SensitiveService s = new SensitiveService();
        s.addWord("色情");
        s.addWord("赌博");
        System.out.println(s.filter("你好色 情sqadwad赌*as博adwda"));

    }
}
