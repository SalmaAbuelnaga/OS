import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Vector;
import javax.security.auth.x500.X500Principal;
public class Interpreter {
	static Hashtable<String, Object> memory=new Hashtable<String, Object>();
	public static void parser(String file) throws Exception {
		String s = readFile(file);
		String[] a = s.split(System.lineSeparator());
		for (int i = 0; i < a.length; i++) {
			parse(a[i]);
		}
	}	
	public static void parse(String v) throws Exception {
		String []s = v.split("\\s+");
		switch(s[0]) {
		case "print": if(s.length!=2) {throw new Exception("invalid instruction");}
		Print(s[1]);break;
		case"assign":
			String r;
			if(s.length==4) {
				if(s[2].equals("readFile")) {
					
					 r=readFile((String)readfromMemory(s[3]));
				}
				else
					throw new Exception("invalid instruction");
			}
			else if (s.length==3) {
				r = s[2];
			}
			else throw new Exception("");
			assign(s[1],r);
			break;
		case "add":
			if(s.length!=3) {throw new Exception("invalid instruction");}
			add(s[1],s[2]);
			break;
		case "writeFile":
			if(s.length!=3) {throw new Exception("invalid instruction");}
			Object a =readfromMemory(s[1]);
			Object b =readfromMemory(s[2]);
			if(a==null && b==null) {
				writeFile(s[1],s[2]);
			}
			else if(a==null&& b!=null) {
				writeFile(s[1],(String)b);
			}
			else if(a!=null && b==null) {
				writeFile((String)a,s[2]);
			}
			else
				writeFile((String)a,(String)b);
			break;
		
		case "readFile":
			if(s.length!=2){throw new Exception("invalid instruction");}
				if(readfromMemory(s[1])==null)
					readFile(s[1]);
				else
					readFile((String)readfromMemory(s[1]));
			
			break; 
		
		}
	}
	public static String input() {
		//System.out.println("please type in input");
		Scanner sc = new Scanner(System.in);
		String str = sc.nextLine();
		return str;
	}
	public static void Print(String s) {
		Object c=null;
		c= readfromMemory(s);
		if (c==null)
				System.out.print(s);
		else
			System.out.print(c);
	}
	
	public static void assign(String x, Object y) {
		if (y instanceof String) {
			if (((String) y).equals("input")) {
				y = input();
			}
			
		}
		writeintoMemory(x, y);
	}
	public static void writeintoMemory(String x, Object y) {
		if (memory.containsKey(x)) {
			memory.replace(x, y);
		}
		else {
			memory.put(x, y);
		}
	}
	public static Object readfromMemory(String x){
		if (!memory.containsKey(x)) {
			System.out.println("no such variable!");
			return null;
		}
		return (memory.get(x));
	}

	@SuppressWarnings("resource")
	public static String readFile(String filename) {
		Vector<String> v = new Vector<String>();
		String s = "";
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filename));
			String line;
			while ((line = reader.readLine()) != null) {
				v.add(line);
				s += line;
				s += "\n";
			}
			//System.out.println(s);
			return s;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error occured while trying to readfile, are you sure file exists?");
			e.printStackTrace();
		}
		return null;
	}
	public static void writeFile(String filename, String x) {
		File f = new File(filename);
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			FileWriter writer = new FileWriter(f, true);
			BufferedWriter br = new BufferedWriter(writer);
			br.write(x);
			br.write('\n');
			br.close();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void add(String x, String y) throws Exception {
		Object c = readfromMemory(x);
		Object d = readfromMemory(y);
		int a;
		int b;
		
		if (c.getClass().getSimpleName().equals("Integer")) {
			a = (int) c;
		} else if (c.getClass().getSimpleName().equals("String")) {
			a = Integer.parseInt((String) c);
		} else {
			throw new Exception("Variable not addable");
		}
		if (d.getClass().getSimpleName().equals("Integer")) {
			b = (int) d;
		} else if (d.getClass().getSimpleName().equals("String")) {
			b = Integer.parseInt((String) d);
		} else {
			throw new Exception("Variable not addable");
		}
		int z = a + b;
		writeintoMemory(x, z );
	}
	public static void main(String[] args) throws Exception {
	parser("src/Program 1.txt");
	parser("src/Program 2.txt");
	parser("src/Program 3.txt");
	}
}
