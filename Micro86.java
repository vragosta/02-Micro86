package pkg02.micro86;
import java.util.*;
import java.io.*;

public class Micro86 {

        static final int memorySize = 5;
        static int [] memoryArray = new int [memorySize];
        static int accumulator = 0000000;
        static int iPointer = 00000000;
        static int flags = 00000000;
        static int iRegister = 00000000;

    public static void main(String[] args) {

        title();
        loader();
        fetch();
        execute();
        fetch();
        execute();
        fetch();
        execute();

        String dis = disassembler();
        System.out.println(dis);
    }

    /*******************
    *PRINTS TITLE MSG
    *RETURNS VOID
    *******************/
    public static void title(){
        System.out.println();
        System.out.println("   Micro86 Emulator \n");
    }

    /****************************************************
    *INITIALIZES ALL ELEMENTS OF ARRAY TO 0
    *RETURNS VOID
    ****************************************************/
    public static void bootUp(){

        for (int i = 0; i < memorySize; i++){
            memoryArray[i] = 0;
        }
    }

    /****************************************************
    *DUMPS THE ELEMENTS OF ARRAY TO SCREEN
    *RETURNS VOID
    ****************************************************/
    public static void memoryDump(){

        System.out.println("MEMORY DUMP");
        for (int i = 0; i < memorySize; i++){
            System.out.print("a[" + i + "] = ");
            System.out.format("%08x%n", memoryArray[i]);
        }
        System.out.println();
    }

    /*******************************************
    *CONVERTS CONTENTS OF REGISTERS TO A STRING
    *RETURNS STRING
    *******************************************/
    public static String registerToString(){

        String s = "";
        s += "Registers: ";
        s += " acc: ";
        s += String.format("%08d", accumulator);
        s += " ip: ";
        s += String.format("%08d", iPointer);
        s += " flags: ";
        s += String.format("%08d", flags);
        s += " ir: ";
        s += String.format("%08x", iRegister);
        return s;
    }

    /********************************************************
    *OPENS FILE HEX.TXT, READS CONTENTS INTO THE MEMORY ARRAY
    *RETURNS VOID
    ********************************************************/
    public static void loader(){

        try{
        File file = new File("hex.txt");
        int i=0;
        Scanner input = new Scanner(file);

           while(input.hasNext()){
                memoryArray[i] = input.nextInt(16);
                i++;
            }
            input.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    /********************************************************
    *EXTRACTS OPCODE AND OPERAND AND GETS MNEMONIC
    *RETURNS STRING
    ********************************************************/
    public static String disassembler(){

        int opCode, operand, i;
        String mnemonic;
        String s = "";
        iPointer = 0;
        iRegister = 0;

        System.out.println("===== Execution Trace =====");
        for (i = 0; i < memorySize; i++){
                opCode = extractOpcode(memoryArray[i]);
                operand = extractOperand(memoryArray[i]);
                mnemonic = getMnemonic(opCode);
                        if (mnemonic == "HALT"){
                                s += String.format("%08x", i);
                                s += ": ";
                                s += mnemonic;
                                s += "\t\t\t\t\t";
                                s += registerToString();
                                s += "\n";

                                iPointer++;
                                iRegister = memoryArray[i];
                        }
                        else if (opCode == 0000){
                                //
                        }
                        else{
                                s += String.format("%08x", i);
                                s += ": ";
                                s += mnemonic;
                                s += "\t\t";
                                s += String.format("%08x", operand);
                                s += "\t\t";
                                s += registerToString();;
                                s += "\n";

                                iPointer++;
                                iRegister = memoryArray[i];
                        }
        }
        return s;
    }

    /********************************************************
    *INPUTS AN INTEGER HEXNUMBER
    *EXTRACTS OPCODE(UPPER 16 BITS)
    *RETURNS INT
    ********************************************************/
    public static int extractOpcode(int instruction){

        int opCode = instruction >>> 16;
        return opCode;
    }

    /********************************************************
    *INPUTS AN INTEGER HEXNUMBER
    *EXTRACTS OPERAND(LOWER 16 BITS)
    *RETURNS INT
    ********************************************************/
    public static int extractOperand(int instruction){

        int operand = instruction & 0x0000FFFF;
        return operand;
    }

    /********************************************************
    *INPUTS AN OPCODE
    *INTERPRETS HEX NUMBER SEARCHES MAP FOR STRING EQUIVALENT
    *RETURNS STRING
    ********************************************************/
    public static String getMnemonic(int opCode){

        Map<String, Integer> opCodeTable = new TreeMap<String, Integer>();
        Map<Integer, String> mnemonicTable = new TreeMap<Integer, String>();

        final int
                HALT  =         0x0100,
                LOAD  =         0x0202,
                LOADI =         0x0201,
                STORE =         0x0302,
                ADD   =         0x0402,
                ADDI  =         0x0401,
                SUB   =         0x0502,
                SUBI  =         0x0501,
                MUL   =         0x0602,
                MULI  =         0x0601,
                DIV   =         0x0702,
                DIVI  =         0x0701,
                MOD   =         0x0802,
                MODI  =         0x0801,
                CMP   =         0x0902,
                CMPI  =         0x0901,
                JMPI  =         0x0A01,
                JEI   =         0x0B01,
                JNEI  =         0x0C01,
                JLI   =         0x0D01,
                JLEI  =         0x0E01,
                JGI   =         0x0F01,
                JGEI  =         0x1001,
                IN    =         0x1100,
                OUT   =         0x1200;

         {
                opCodeTable.put("HALT", HALT);
                opCodeTable.put("LOAD", LOAD);
                opCodeTable.put("LOADI", LOADI);
                opCodeTable.put("STORE", STORE);
                opCodeTable.put("ADD",  ADD);
                opCodeTable.put("ADDI", ADDI);
                opCodeTable.put("SUB",  SUB);
                opCodeTable.put("SUBI", SUBI);
                opCodeTable.put("MUL",  MUL);
                opCodeTable.put("MULI", MULI);
                opCodeTable.put("DIV",  DIV);
                opCodeTable.put("DIVI", DIVI);
                opCodeTable.put("MOD",  MOD);
                opCodeTable.put("MODI", MODI);
                opCodeTable.put("CMP",  CMP);
                opCodeTable.put("CMPI", CMPI);
                opCodeTable.put("JMPI", JMPI);
                opCodeTable.put("JEI",  JEI);
                opCodeTable.put("JNEI", JNEI);
                opCodeTable.put("JLI",  JLI);
                opCodeTable.put("JLEI", JLEI);
                opCodeTable.put("JGI",  JGI);
                opCodeTable.put("JGEI", JGEI);
                opCodeTable.put("IN",   IN);
                opCodeTable.put("OUT",  OUT);

                for (String mnemonic : opCodeTable.keySet())
                        mnemonicTable.put(opCodeTable.get(mnemonic), mnemonic);
        }
                String mnc = mnemonicTable.get(opCode);
                return mnc;
   }

    /********************************************************
    *FETCHES THE WORD FROM MEMORY, INCREMENTS IPOINTER
    *RETURNS VOID
    ********************************************************/
    public static void fetch(){

        iRegister = memoryArray[iPointer];
        iPointer++;
    }

    /********************************************************
    *EXTRACTS OPCODE AND OPERAND FROM IREGISTER
    *PRINTS POST-MORTEM DUMP
    *RETURNS
    ********************************************************/
    public static void execute(){

        int opCode, operand, i;
        String mnemonic;

        opCode = extractOpcode(iRegister);
        operand = extractOperand(iRegister);
        mnemonic = getMnemonic(opCode);
                        if (mnemonic == "HALT"){
                                System.out.println("\t" + mnemonic + "\n");
                                System.out.println("===== Post-Mortem Dump (normal termination) =====");
                                String str = registerToString();
                                System.out.println("--------------------");
                                System.out.println(str);
                                System.out.println("----------Memory----------");
                                  for (i = 0; i < memorySize; i++){
                                        System.out.format("%08x", i);
                                        System.out.print(": ");
                                        System.out.format("%08x%n", memoryArray[i]);
                                  }
                                System.out.println("-------------------------");
                        }
                        else if (opCode == 0000){
                                System.out.println("VAR" +  "\t\t" + operand);
                        }
                        else
                        System.out.println("\t" + mnemonic + "\t" + operand);
    }
}

