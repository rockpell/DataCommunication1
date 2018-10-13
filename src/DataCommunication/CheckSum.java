package DataCommunication;

import java.io.*;
import java.util.LinkedList;

// https://kutar37.tistory.com/29
// http://kylog.tistory.com/6
// http://www.ktword.co.kr/abbr_view.php?m_temp1=1477
// http://mwultong.blogspot.com/2006/09/java-10-2binary.html
// http://mwultong.blogspot.com/2006/10/java-text-file-write.html

public class CheckSum {
    public static void main(String[] args){

        LinkedList<Integer> bitList = new LinkedList<Integer>();
        CheckSum checkSum = new CheckSum();

        try{
            File file = new File("./src/input.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String _line;

            _line = bufferedReader.readLine();

            char[] _charArray = _line.toCharArray();

            checkSum.InputBitsToList(bitList, _charArray);

            for (int i : bitList) {
                System.out.println(i);
            }

            int _result = checkSum.CalculateCheckSum(bitList);
            String _output = "";

            System.out.println("Before Binary : " + _result);
            _output = checkSum.FillBitZero(Integer.toBinaryString(_result));
            System.out.println(_output);

            BufferedWriter out = new BufferedWriter(new FileWriter("output_checksum.txt"));
            out.write(_output);

            out.close();
        }catch (IOException e){
            System.out.println(e);
        }
    }

    public void InputBitsToList(LinkedList<Integer> list, char[] chars){ // input the bits in ther list
        for(int i = 0; i < chars.length; i += 2){
            list.add(MergeTo16Bit(Change8Bit((int)chars[i]), Change8Bit((int)chars[i + 1])) );
        }
    }

    public int CalculateCheckSum(LinkedList<Integer> list){
        return ComplementOne(SumBits(list));
    }

    public String FillBitZero(String text){ // Fill 0
        String _result = "";

        for(int i = 0; i < 16 - text.length(); i++){
            _result += "0";
        }
        _result += text;

        return _result;
    }

    private int ComplementOne(int value){ // 1's complement
        int _result = 0;
        _result = value ^ 65535; // 65535 == 1111 1111 1111 1111
        return  _result;
    }

    private int SumBits(LinkedList<Integer> list){ // sum the bits in the list
        int _result = 0;
        for(int bit : list){
            _result += bit;
            _result = Prevent16BitOverFlow(_result);
        }

        return _result;
    }

    private int Prevent16BitOverFlow(int value){ // 2^16 = 65536
        int _result = 0;
        int _quotient = value / 65536;

        _result = value - (_quotient * 65536) + _quotient;

        return _result;
    }

    private int Change8Bit(int value){ // Add parity bytes to 7 bits
        int _result;
        _result  = value << 1;

        if(CountOneBit(value) % 2 != 0){
            _result += 1;
        }
        return _result;
    }

    private int CountOneBit(int value){ // count 1 bit
        int i;
        for(i = 0; value != 0; i++){
            value &= (value - 1);
        }
        Integer.toBinaryString(1);
        return i;
    }

    private int MergeTo16Bit(int value1, int value2){ // merge two 8bit
        int _result = 0;
        _result = (value1 << 8) + value2;
        return _result;
    }

}