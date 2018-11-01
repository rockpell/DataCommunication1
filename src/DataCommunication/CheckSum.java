package DataCommunication;

import java.io.*;
import java.util.LinkedList;

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

            System.out.println("input : " + _line);

            char[] _charArray = _line.toCharArray();

            if(checkSum.InputBitsToList(bitList, _charArray)){
                int _result = checkSum.CalculateCheckSum(bitList);
                String _output = "";

                System.out.println("Before Binary : " + _result);
                _output = checkSum.FillBitZero(Integer.toBinaryString(_result));
//                _output = Integer.toBinaryString(_result);
                System.out.println("output : " + _output);

                BufferedWriter out = new BufferedWriter(new FileWriter("./src/output_checksum.txt"));
                out.write(_output);

                out.close();
            } else {
                BufferedWriter out = new BufferedWriter(new FileWriter("./src/output_checksum.txt"));
                out.write("There is not enough data. Please enter in 16-bit units");

                out.close();
            }
        }catch (IOException e){
            System.out.println(e);
        }
    }

    public boolean InputBitsToList(LinkedList<Integer> list, char[] chars){ // input the bits in ther list
        if(chars.length % 2 == 0){
            for(int i = 0; i < chars.length; i += 2){
                list.add(MergeTo16Bit(Change8Bit((int)chars[i]), Change8Bit((int)chars[i + 1])) );
            }
            return true;
        } else {
            System.out.println("There is not enough data. Please enter in 16-bit units");
            return false;
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