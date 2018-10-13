package DataCommunication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class CRC {
    public static void main(String[] args){

        LinkedList<Boolean> linkedList = new LinkedList<Boolean>();
        CRC crc = new CRC();

        try {
            File file = new File("./src/input.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String _line;

            _line = bufferedReader.readLine();

            char[] _charArray = _line.toCharArray();

            crc.InputBitsToList(linkedList, _charArray);

            for(boolean b : linkedList){
                if(b) System.out.print("1");
                else System.out.print("0");
            }

        }catch (IOException e){
            System.out.println(e);
        }
    }

    public void InputBitsToList(LinkedList<Boolean> list, char[] chars){ // input the bits in ther list
        int _temp = 0;
        String _tempString = "";
        char[] _tempChar;
        for(int i = 0; i < chars.length; i++){
            _temp = Change8Bit((int)chars[i]);
            _tempString = Integer.toBinaryString(_temp);
            _tempString = Fill8BitZero(_tempString);
            _tempChar = _tempString.toCharArray();
            for(int p = 0; p < _tempChar.length; p++){
                if(_tempChar[p] == '0'){
                    list.add(false);
                } else {
                    list.add(true);
                }
            }
        }
    }

    private String Fill8BitZero(String text){ // Fill 0
        String _result = "";

        for(int i = 0; i < 8 - text.length(); i++){
            _result += "0";
        }
        _result += text;

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
        return i;
    }
}
