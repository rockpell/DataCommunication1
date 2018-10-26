package DataCommunication;

import java.io.*;
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

            System.out.println("input : " + _line);

            char[] _charArray = _line.toCharArray();

            crc.InputBitsToList(linkedList, _charArray);
            crc.CRC16(linkedList);

            String _crcResult = crc.ChangeBitListToString(linkedList);

            System.out.println("output : " + _crcResult);

            BufferedWriter out = new BufferedWriter(new FileWriter("src/output_crc.txt"));
            out.write(_crcResult);

            out.close();
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

    public void CRC16(LinkedList<Boolean> list){
        LinkedList<Boolean> _list = new LinkedList<Boolean>();
        for(int i = 0; i < 16; i++) list.add(false); // add 0bit 16time
        CreateCRC16GList(_list);

        while(list.size() > 16){
            XorOperation(list, _list);
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

    private void CreateCRC16GList(LinkedList<Boolean> list){
        for(int i = 0; i < 17; i++){
            if(i == 0 || i == 1 || i == 14 || i == 16){
                list.add(true);
            } else {
                list.add(false);
            }
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

    private String Fill8BitZero(String text){ // Fill 0
        String _result = "";

        for(int i = 0; i < 8 - text.length(); i++){
            _result += "0";
        }
        _result += text;

        return _result;
    }
}
