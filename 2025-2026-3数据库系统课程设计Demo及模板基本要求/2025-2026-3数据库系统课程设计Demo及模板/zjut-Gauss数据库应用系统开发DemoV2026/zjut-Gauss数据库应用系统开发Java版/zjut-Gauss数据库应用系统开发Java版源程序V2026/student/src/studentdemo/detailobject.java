package studentdemo;

public class detailobject {
	private String  repsno;
	private String  corname;
	private String  repgrade;
	private String  cornber;
    public detailobject (String a, String b,String c,String d){
    	repsno=a;
    	corname=b;
    	repgrade=c;
    	cornber=d;	
    }
    public String tocornber(){
        return cornber;
 }
    public String torepsno(){
        return repsno;
 }
}