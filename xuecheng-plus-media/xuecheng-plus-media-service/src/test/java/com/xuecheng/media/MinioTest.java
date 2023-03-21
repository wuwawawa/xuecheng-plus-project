package com.xuecheng.media;

import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import io.minio.*;
import io.minio.errors.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

/**
 * @author Mr.M
 * @version 1.0
 * @description 测试minio的sdk
 * @date 2023/2/17 11:55
 */
public class MinioTest {

    MinioClient minioClient =
            MinioClient.builder()
                    .endpoint("http://110.42.143.32:9000")
                    .credentials("244g6XDcocjDhnwK", "kQ6wo7wf4W58KXP32ewxHQIcKg03tR4a")
                    .build();

    @Test
    public void test_upload() throws Exception {

        //上传文件的参数信息
        UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                .bucket("testbucket")//桶
                .filename("/Users/fortune/代码整洁之道.pdf") //指定本地文件路径
                .object("代码整洁之道.pdf")//对象名 放在子目录下
                .build();

        //上传文件
        minioClient.uploadObject(uploadObjectArgs);

    }

    //删除文件
    @Test
    public void test_delete() throws Exception {
        //RemoveObjectArgs
        RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder().bucket("testbucket").object("代码整洁之道.pdf").build();
        //删除文件
        minioClient.removeObject(removeObjectArgs);

    }

    //查询文件 从minio中下载
    @Test
    public void test_getFile() throws Exception {

        GetObjectArgs getObjectArgs = GetObjectArgs.builder().bucket("testbucket").object("代码整洁之道.pdf").build();
        //查询远程服务获取到一个流对象
        FilterInputStream inputStream = minioClient.getObject(getObjectArgs);
        //指定输出流
        FileOutputStream outputStream = new FileOutputStream(new File("/Users/fortune/Downloads/代码整洁之道.pdf"));
        IOUtils.copy(inputStream, outputStream);

        //校验文件的完整性对文件的内容进行md5
        FileInputStream fileInputStream1 = new FileInputStream(new File("/Users/fortune/代码整洁之道.pdf"));
        String source_md5 = DigestUtils.md5Hex(fileInputStream1);
        FileInputStream fileInputStream = new FileInputStream(new File("/Users/fortune/Downloads/代码整洁之道.pdf"));
        String local_md5 = DigestUtils.md5Hex(fileInputStream);
        if (source_md5.equals(local_md5)) {
            System.out.println("下载成功");
        }

    }

    /*
    字符          数值
    I             1
    V             5
    X             10
    L             50
    C             100
    D             500
    M             1000

    I可以放在V(5) 和X(10) 的左边，来表示 4 和 9。
    X可以放在L(50) 和C(100) 的左边，来表示 40 和90。
    C可以放在D(500) 和M(1000) 的左边，来表示400 和900。

    输入: num = 58
    输出: "LVIII"
    解释: L = 50, V = 5, III = 3.

    输入: num = 1994
    输出: "MCMXCIV"
    解释: M = 1000, CM = 900, XC = 90, IV = 4.

    1 <= num <= 3999
     */

    @Test
    public String intToRoman(int num) {
        HashMap<Integer, String> map = new HashMap<>();
        //hashmap初始化
        map.put(1, "I");
        map.put(2, "II");
        map.put(3, "III");
        map.put(5, "V");
        map.put(10, "X");
        map.put(50, "L");
        map.put(100, "C");
        map.put(500, "D");
        map.put(1000, "M");

        int[] bits = new int[4];

        //给bits赋值
        int start = 0;
        while (num != 0) {
            int tmp = num % 10;
            bits[start++] = tmp;
            num /= 10;
        }
        for (int i : bits) {
            System.out.println(i + "  ");
        }

        StringBuilder res = new StringBuilder();
        //判断千位数字是否为0
        if (bits[3] != 0) {
            for (int i = 0; i < bits[3]; i++) {
                res.append('M');
            }
        }
        //判断百位数字是否为0
        int bit2 = bits[2];
        if (bit2 != 0) {
            if (bit2 < 4) {
                for (int i = 0; i < bit2; i++) {
                    res.append("C");
                }
            }
            if (bit2 == 4) {
                res.append("CD");
            }

            if (bit2 == 5) {
                res.append("D");
            }
            if (bit2 == 9) {
                res.append("CM");
            }

            if (bit2 > 5 && bit2 < 9) {
                res.append("D");
                int i = bit2 - 5;
                for (int j = 0; j < i; j++) {
                    res.append("C");
                }
            }
        }
        //判断10位数字是否为0
        int bit1 = bits[1];
        if (bit1 != 0) {
            if (bit1 < 4) {
                for (int i = 0; i < bit1; i++) {
                    res.append("X");
                }
            }
            if (bit1 == 4) {
                res.append("XL");
            }

            if (bit1 == 5) {
                res.append("L");
            }
            if (bit1 == 9) {
                res.append("XC");
            }

            if (bit1 > 5 && bit1 < 9) {
                res.append("L");
                int i = bit1 - 5;
                for (int j = 0; j < i; j++) {
                    res.append("X");
                }
            }
        }
        //判断位数字是否为0
        int bit0 = bits[0];
        if (bit0 != 0) {
            if (bit0 < 4) {
                for (int i = 0; i < bit0; i++) {
                    res.append("I");
                }
            }
            if (bit0 == 4) {
                res.append("IV");
            }

            if (bit0 == 5) {
                res.append("V");
            }
            if (bit0 == 9) {
                res.append("IX");
            }

            if (bit0 > 5 && bit0 < 9) {
                res.append("V");
                int i = bit0 - 5;
                for (int j = 0; j < i; j++) {
                    res.append("I");
                }
            }
        }


        return res.toString();
    }


    @Test
    public void testStart() {
        String s = intToRoman(58);
        System.out.println(s);
    }


}
