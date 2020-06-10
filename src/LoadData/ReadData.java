package LoadData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.ArrayList;

public class ReadData {

	public ArrayList<String> readData () {
		ArrayList<String> listST = new ArrayList<String>();
		try {
			BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream("data//person.csv"), "UTF-8"));
			String line;
			String result="";
			while ((line = bf.readLine()) != null) {
				result += line+"\n";
			}
			String[] arrObject = result.split("\n");
			//loai bo header
			for (int i = 1; i < arrObject.length; i++) {
				listST.add(arrObject[i]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return listST;
	}

	public static void main(String[] args) {
		ReadData read = new ReadData();
		ArrayList<String> listST = read.readData();
		for (String string : listST) {
			System.out.println(string);
		}
	}
}
