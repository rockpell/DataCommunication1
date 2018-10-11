package DataCommunication;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// https://kutar37.tistory.com/29
// http://kylog.tistory.com/6
// http://www.ktword.co.kr/abbr_view.php?m_temp1=1477

public class CheckSum {
    public static void main(String[] args){
        List<Integer> bitList = new ArrayList<Integer>();
        CheckSum checkSum = new CheckSum();

        try{
            File file = new File("./src/input.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;

            line = bufferedReader.readLine();

            char[] charArray = line.toCharArray();

            checkSum.InputBitsToList(bitList, charArray);

            for (int i : bitList) {
                System.out.println(i);
            }

            System.out.println(checkSum.CalculateCheckSum(bitList));

        }catch (IOException e){
            System.out.println(e);
        }
    }

    private int CountOneBit(int value){
        int i;
        for(i = 0; value != 0; i++){
            value &= (value - 1);
        }
        return i;
    }

    private int Change8Bit(int value){ // Add parity bytes to 7 bits
        int _result;
        _result  = value << 1;

        if(CountOneBit(value) % 2 != 0){
            _result += 1;
        }
        return _result;
    }

    private int MergeTo16Bit(int value1, int value2){
        int _result = 0;
        _result = (value1 << 8) + value2;
        return _result;
    }

    public void InputBitsToList(List<Integer> list, char[] chars){
        for(int i = 0; i < chars.length; i += 2){
            list.add(MergeTo16Bit(Change8Bit((int)chars[i]), Change8Bit((int)chars[i + 1])) );
        }
    }

    private int SumBits(List<Integer> list){
        int _result = 0;
        for(int bit : list){
            _result += bit;
            System.out.println("캐리 날리기 전 : " + _result);
            _result = Prevent16BitOverFlow(_result);
            System.out.println("캐리 날린 후 : " + _result);
        }

        return _result;
    }

    private int Prevent16BitOverFlow(int value){ // 2^16 = 65536
        int _result = 0;
        int _quotient = value / 65536;

        _result = value - (_quotient * 65536) + _quotient;

        return _result;
    }

    private int ComplementOne(int value){
        int _result = 0;
        _result = value ^ 65535; // 65535 == 1111 1111 1111 1111
        return  _result;
    }

    public int CalculateCheckSum(List<Integer> list){
        return ComplementOne(SumBits(list));
    }
}