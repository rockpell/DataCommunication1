package DataCommunication;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class CRC {
    public static void main(String[] args){

        LinkedList<Boolean> linkedList = new LinkedList<Boolean>(); // 0 : false, 1 : true
        CRC crc = new CRC();

        try {
            File file = new File("./src/input.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String _line;

            _line = bufferedReader.readLine();

            char[] _charArray = _line.toCharArray();

            crc.InputBitsToList(linkedList, _charArray);

            System.out.println("input value : ");

            for(boolean b : linkedList){
                if(b) System.out.print("1");
                else System.out.print("0");
            }

            System.out.println("");

            crc.CRC16(linkedList);

            String _crcResult = crc.ChangeBitListToString(linkedList);
            _crcResult = crc.Fill16BitZero(_crcResult);

            System.out.println("result : " + _crcResult);
        }catch (IOException e){
            System.out.println(e);
        }
    }

    public String ChangeBitListToString(LinkedList<Boolean> list){
        String _result = "";

        for(boolean b : list){
            if(b) _result += "1";
            else _result += "0";
        }

        return _result;
    }

    public void CRC16(LinkedList<Boolean> list){
        LinkedList<Boolean> _list = new LinkedList<Boolean>();
        for(int i = 0; i < 16; i++) list.add(false); // add 0bit 16time
        CreateCRC16GList(_list);

        for(boolean b : list){
            if(b) System.out.print("1");
            else System.out.print("0");
        }

        while(list.size() > 16){
            XorOperation(list, _list);
        }

        System.out.println("");

        for(boolean b : _list){
            if(b) System.out.print("1");
            else System.out.print("0");
        }

        System.out.println("");

        for(boolean b : list){
            if(b) System.out.print("1");
            else System.out.print("0");
        }
    }

    private void XorOperation(LinkedList<Boolean> inputList, LinkedList<Boolean> gList){
        for(int i = 0; i < gList.size(); i++){
            inputList.set(i, inputList.get(i) ^ gList.get(i));
        }
        ZeroOut(inputList);
    }

    private void ZeroOut(LinkedList<Boolean> list){
        if(!list.get(0)){
            do{
                list.pollFirst();
            }while(!list.get(0));
        }
    }

    private void CreateCRC16GList(LinkedList<Boolean> list){
        for(int i = 0; i < 17; i++){
            if(i == 0 || i == 1 || i == 14 || i == 16){
                list.add(true);
            } else {
                list.add(false);
            }
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

    public String Fill16BitZero(String text){ // Fill 0
        String _result = "";

        for(int i = 0; i < 16 - text.length(); i++){
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
