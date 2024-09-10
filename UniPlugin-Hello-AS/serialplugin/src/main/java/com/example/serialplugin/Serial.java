package com.example.serialplugin;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fazecast.jSerialComm.SerialPort;

import java.io.InputStream;
import java.io.OutputStream;

import io.dcloud.feature.uniapp.annotation.UniJSMethod;
import io.dcloud.feature.uniapp.bridge.UniJSCallback;
import io.dcloud.feature.uniapp.common.UniModule;
public class Serial {
    public SerialPort serialPort;

    /**
     * 创建连接
     * @param CommName
     * @param rate
     * @param bit
     * @param stop
     * @param Parity
     * @param callback
     */
    @UniJSMethod(uiThread = false)
    public void crateSerialPort(String CommName,int rate,int bit,int stop,int Parity,UniJSCallback callback) {
        try{
            serialPort = SerialPort.getCommPort(CommName); // 替换为你的串口名称
            serialPort.setComPortParameters(rate, bit, stop, Parity); // 设置波特率为9600, 数据位8位, 停止位1, 无校验位
            serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0); // 设置超时
            callback.invoke(1);
        }catch (Exception e){
            callback.invoke(-1);
        }
    }
    /**
     * 将十六进制字符串转换为byte数组
     *
     * @param s 十六进制字符串
     * @return byte数组
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2]; // 因为每两个十六进制字符代表一个byte
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
    /**
     * 写入数据
     */
    @UniJSMethod(uiThread = false)
    public void writeSerialPort(String hexString,UniJSCallback callback) {
        try {
            byte[] byteArray = hexStringToByteArray(hexString);
            OutputStream outputStream = serialPort.getOutputStream();
            outputStream.write(byteArray);
            InputStream inputStream = serialPort.getInputStream();

        }catch (Exception e){
            callback.invoke(-1);
        }
    }

    /**
     * 打开端口
     */
    @UniJSMethod(uiThread = false)
    public void openSerialPort(UniJSCallback callback) {
        try {
            serialPort.openPort();
            callback.invoke(1);
        }catch (Exception e){
            callback.invoke(-1);
        }
    }
    /**
     * 关闭端口
     */
    @UniJSMethod(uiThread = false)
    public void closeSerialPort(UniJSCallback callback) {
        try {
            serialPort.closePort();
            callback.invoke(1);
        }catch (Exception e){
            callback.invoke(-1);
        }
    }
    /**
     * 返回所有串口端口
     * @param callback
     */
    @UniJSMethod(uiThread = false)
    public void getSerialPorts(UniJSCallback callback) {
        SerialPort[] commPorts = SerialPort.getCommPorts();
        callback.invoke(JSON.toJSONString(commPorts));
    }
}
