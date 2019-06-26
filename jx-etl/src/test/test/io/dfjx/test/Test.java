package io.dfjx.test;

public class Test {
	public static void main(String[] args) {
		String runScript="test0100.sh";
		String start="";
		String end="";
		int ind=-1;
		ind=runScript.lastIndexOf("0100.");
		if(ind>=0){
			start=runScript.substring(0, ind);
			end=runScript.substring(ind+4, runScript.length());
		}
		System.out.println(start+end);
	}
}