package MUA;

/*
 * MUA编辑器第二阶段
 * 姓名：桂晓琬
 * 学号：3150104802
 * 日期：2017／11/29
 * 
 * 
 * */

//数字
//单词 “开头 ”为止到（空格 tab 回车）包含结束
//列表到字面量以【】包含 元素以空格分隔
import java.util.Scanner;
//枚举类型可能以后会用到
enum readin{
	make, thing, erase, isname, pring, read, readlist
}
enum type{
	number, string, floattype, bool, listtype, othertype;
}
//word类 每个word有一个name和value 都以string形式存储
//以后可能会用到wordtype，目前可以直接分辨
class Word{
	public String name=null;
	//public type wordType;
	public String value=null;
}
//程序入口
public class MUA {
	public static Word[] wordlist=new Word[256];
	public static int point=0;
	//程序入口
	public static void main(String[] args) {
		Reader reader= new Reader();
		while(true) {
			//每次进行读取不断重复
			reader.readin();
		}
	}
	
}
class Reader{
	Scanner input = new Scanner(System.in);
	
	public void readin() {
		System.out.print('>');
		//每次读取一行内容
		String instr = input.nextLine();
		//System.out.println(explainer(instr));
		//读取的一行内容交给解释器进行解释
		explainer(instr);
	}
	private String explainer(String instr) {
		switch(instr.charAt(0)) {
		//进行注释消解
		case '/':
			if(instr.length()>=2 && instr.charAt(1)=='/') {
				return null;
			}
		//m打头的三种操作
		case 'm'://make mod mul  
			//进行make操作
			if(instr.length()>=5 && instr.substring(0,5).equals("make ")) {
				//MUA.wordlist[MUA.point]=new Word();
				//MUA.wordlist[MUA.point]=make(instr.substring(5));
				//MUA.point++;
				return make(instr.substring(5));
			}
			//进行mod操作
			else if(instr.length()>=4 && instr.substring(0,4).equals("mod ")) {
				return mod(instr.substring(4));
			}
			//进行乘法操作
			else if(instr.length()>=4 && instr.substring(0,4).equals("mul ")) {
				return mul(instr.substring(4));
			}
		case 't':
			//进行thing取数操作
			if(instr.length()>=6 &&instr.substring(0,6).equals("thing ")) {
				if(instr.charAt(6)!='"') {return thing(instr.substring(6));}
				else {return thing(instr.substring(7));}
			}
			//else return null;
			//break;
		case ':':
			//进行：取数操作
			return thing(instr.substring(1));
			//break;
		case 'e'://erase
			//进行消除操作
			if(instr.length()>=6 && instr.substring(0,6).equals("erase ")) {
				return erase(instr.substring(6));
			}
			else if(instr.length()>=3 && instr.substring(0,3).equals("eq "))//eq
			{
				return eq(instr.substring(3));
			}
			//else return null;
			//break;
		case 'i':
			//查找是否有该名字的变量存在
			if(instr.length()>=7 && instr.substring(0,7).equals("isname ")) {
				return isname(instr.substring(7));
			}
			//else return null;
			//break;
		case 'p':
			//打印输出
			if(instr.length()>=6 && instr.substring(0,6).equals("print ")) {
				print(instr.substring(6));
			}
			//else return null;
			//break;
		case 'r':
			//读一行作为list
			if(instr.length()>=8 && instr.substring(0,8).equals("readlist")) {
				return readlist();
			}
			//读入一个单词
			else if(instr.length()>=4 && instr.substring(0,4).equals("read")) {
				return read();
			}
			
		case 'a'://add
			//加法
			if(instr.length()>=4 && instr.substring(0,4).equals("add ")) {
				return add(instr.substring(4));
			}
			//and操作
			else if(instr.length()>=4 && instr.substring(0,4).equals("and "))
			{
				return and(instr.substring(4));
			}
			//else return null;
			//break;
		case 's'://sub
			if(instr.length()>=4 && instr.substring(0,4).equals("sub ")) {
				return sub(instr.substring(4));
			}
			//break;
		case 'd'://div
			//除法取整
			if(instr.length()>=4 && instr.substring(0,4).equals("div ")) {
				return div(instr.substring(4));
			}
			//break;
		case 'g'://gt
			//比较大
			if(instr.length()>=3 && instr.substring(0,3).equals("gt ")) {
				return gt(instr.substring(3));
			}
			//break;
		case 'l'://lt
			//比较小
			if(instr.length()>=3 && instr.substring(0,3).equals("lt ")) {
				return lt(instr.substring(3));
			}
			//break;
		case 'o'://or
			//或操作
			if(instr.length()>=3 && instr.substring(0,3).equals("or ")) {
				return or(instr.substring(3));
			}
			//break;
		case 'n'://not
			//非操作
			if(instr.length()>=4 && instr.substring(0,4).equals("not ")) {
				return not(instr.substring(4));
			}
			//break;
		default:
			//返回值
			//单词的字面量
			if(instr.charAt(0)=='"') return instr.substring(1);
			//list的字面量 特别的 如果list后面还有内容会被忽略掉……
			else if(instr.charAt(0)=='[') return explainer(instr.substring(1,instr.indexOf(']')));
			//普通的东西
			else return instr;
		}
	}
	//乘法 输入待乘内容
	private String mul(String instr) {
		//flag1 和 flag2 分别表示两个待乘数
		int flag1,flag2;
		//第一次explainer找到第一个数
		String str0=explainer(instr);
		String str1=str0.substring(0,str0.indexOf(' '));
		flag1=Integer.parseInt(str1);
		//把第一个数去掉，接下来的部分再次explain 找到第二个乘数
		String str2=explainer(str0.substring(str0.indexOf(' ')+1));
		//如果第二个乘数后面没有内容，直接返回结果
		if(str2.indexOf(' ')<0) flag2=Integer.parseInt(str2);
		else flag2=Integer.parseInt(str2.substring(0,str2.indexOf(' ')));
		//如果第二个乘数后面有内容，则需要把这部分内容拼接在结果后面
		if(str2.indexOf(' ')>0) return String.valueOf(flag1*flag2)+str2.substring(str2.indexOf(' '));
		return String.valueOf(flag1*flag2);
	}
	//mod 取余数
	private String mod(String instr) {
		//flag1 和 flag2 分别表示除数和被除数
		int flag1,flag2;
		//第一次explainer找到被除数
		String str0=explainer(instr);
		String str1=str0.substring(0,str0.indexOf(' '));
		flag1=Integer.parseInt(str1);
		//把第一个数去掉，接下来的部分再次explain 找到除数
		String str2=explainer(str0.substring(str0.indexOf(' ')+1));
		//如果除数后面没有内容，直接返回结果
		if(str2.indexOf(' ')<0) flag2=Integer.parseInt(str2);
		else flag2=Integer.parseInt(str2.substring(0,str2.indexOf(' ')));
		//如果除数后面没有内容，直接返回结果
		if(str2.indexOf(' ')>0) return String.valueOf(flag1%flag2)+str2.substring(str2.indexOf(' '));
		return String.valueOf(flag1%flag2);
	}
	//非操作
	private String not(String instr) {
		//explain找到内容的true或false
		String str1=explainer(instr);
		//如果是true
		if(str1.length()>=3 && str1.charAt(0)=='t' && str1.charAt(1)=='r' && str1.charAt(2)=='u' && str1.charAt(3)=='e') {
			//如果变量后面还有内容则拼接
			if(str1.indexOf(' ')>0) {
				return "false"+str1.substring(str1.indexOf(' '));
			}
			//如果变量后面没有内容则直接返回
			else return "false";
		}
		//如果是false
		else if(str1.length()>=4 && str1.charAt(0)=='f' && str1.charAt(1)=='a' && str1.charAt(2)=='l' && str1.charAt(3)=='s'&& str1.charAt(4)=='e') {
			if(str1.indexOf(' ')>0) {
				return "true"+str1.substring(str1.indexOf(' '));
			}
			else return "true";
		}
		else{
			return "value is not bool";
		}
	}
	//或操作
	private String or(String instr) {
		int flag1,flag2;
		//explain找到第一个bool变量
		String str1=explainer(instr);
		//如果是true
		if(str1.length()>=3 && str1.charAt(0)=='t' && str1.charAt(1)=='r' && str1.charAt(2)=='u' && str1.charAt(3)=='e') {
			flag1=1;
		}
		//如果是false
		else if(str1.length()>=4 && str1.charAt(0)=='f' && str1.charAt(1)=='a' && str1.charAt(2)=='l' && str1.charAt(3)=='s'&& str1.charAt(4)=='e') {
			flag1=0;
		}
		//如果是奇怪的东西
		else{
			return "value1 is not bool";
		}
		//explain出第二个变量内容
		String str2=explainer(str1.substring(str1.indexOf(' ')+1));
		if(str2.length()>=3 && str2.charAt(0)=='t' && str2.charAt(1)=='r' && str2.charAt(2)=='u' && str2.charAt(3)=='e') {
			flag2=1;
		}
		else if(str2.length()>=4 && str2.charAt(0)=='f' && str2.charAt(1)=='a' && str2.charAt(2)=='l' && str2.charAt(3)=='s'&& str2.charAt(4)=='e') {
			flag2=0;
		}
		else {
			return "value2 is not bool";
		}
		//因为是or操作 只要两者有一个为真即可
		if(flag1+flag2>=1) {
			//如果第二个后面还有内容进行拼接 否则直接返回
			if(str2.indexOf(' ')>0) {
				return "true"+str2.substring(str2.indexOf(' '));
			}
			else return "true";
		}
		else {
			if(str2.indexOf(' ')>0) {
				return "false"+str2.substring(str2.indexOf(' '));
			}
			else return "false";
		}
	}
	//less than操作 比较第一个是否比第二个小
	private String lt(String instr) {//less than
		int flag1,flag2;
		//找到第一个变量
		String str0=explainer(instr);
		String str1=str0.substring(0,str0.indexOf(' '));
		//判断第一个变量是否为数字，如果不是数字则按照字符串的规则进行比较
		if(!Character.isDigit(str1.charAt(0))) {
			//找到第二个变量
			String str2=explainer(str0.substring(str0.indexOf(' ')+1));
			if(str2.indexOf(' ')<0) {
				if(str1.compareTo(str2)<0) return "true";
				else return "false";
			}
			//如果第二个变量后面有内容进行拼接
			else {
				if(str1.compareTo(str2.substring(0,str2.indexOf(' ')))<0) return "true"+str2.substring(str2.indexOf(' '));
				else return "false"+str2.substring(str2.indexOf(' '));
			}
		}
		//如果第一个变量是数字，则转换为数字直接比较大小
		//但是如果第一个是数字第二个不是，则会出现错误情况
		else {
			flag1=Integer.parseInt(str1);
			String str2=explainer(str0.substring(str0.indexOf(' ')+1));
			if(str2.indexOf(' ')<0) {
				//把字符串直接转换成数字
				flag2=Integer.parseInt(str2);
				if(flag1<flag2) return "true";
				else return "false";
			}
			else {
				flag2=Integer.parseInt(str2.substring(0,str2.indexOf(' ')));
				if(flag1<flag2) return "true"+str2.substring(str2.indexOf(' '));
				else return "false"+str2.substring(str2.indexOf(' '));
			}
		}
		
	}
	//greater than操作 比较第一个是否比第二个大 基本思路和less than一样
	private String gt(String instr) {
		int flag1,flag2;
		
		String str0=explainer(instr);
		String str1=str0.substring(0,str0.indexOf(' '));
		if(!Character.isDigit(str1.charAt(0))) {
			String str2=explainer(str0.substring(str0.indexOf(' ')+1));
			if(str2.indexOf(' ')<0) {
				if(str1.compareTo(str2)>0) return "true";
				else return "false";
			}
			else {
				if(str1.compareTo(str2.substring(0,str2.indexOf(' ')))>0) return "true"+str2.substring(str2.indexOf(' '));
				else return "false"+str2.substring(str2.indexOf(' '));
			}
		}
		else {
			flag1=Integer.parseInt(str1);
			String str2=explainer(str0.substring(str0.indexOf(' ')+1));
			if(str2.indexOf(' ')<0) {
				flag2=Integer.parseInt(str2);
				if(flag1>flag2) return "true";
				else return "false";
			}
			else {
				flag2=Integer.parseInt(str2.substring(0,str2.indexOf(' ')));
				if(flag1>flag2) return "true"+str2.substring(str2.indexOf(' '));
				else return "false"+str2.substring(str2.indexOf(' '));
			}
		}
		
	}
	//div除法取整操作
	private String div(String instr) {
		//flag1 和 flag2 分别表示除数和被除数
		int flag1,flag2;
		String str0=explainer(instr);
		String str1=str0.substring(0,str0.indexOf(' '));
		flag1=Integer.parseInt(str1);
		//把第一个数去掉，接下来的部分再次explain 找到除数
		String str2=explainer(str0.substring(str0.indexOf(' ')+1));
		//如果除数后面没有内容，直接返回结果
		if(str2.indexOf(' ')<0) flag2=Integer.parseInt(str2);
		else flag2=Integer.parseInt(str2.substring(0,str2.indexOf(' ')));
		//如果除数后面没有内容，直接返回结果
		if(str2.indexOf(' ')>0) return String.valueOf(flag1/flag2)+str2.substring(str2.indexOf(' '));
		return String.valueOf(flag1/flag2);
	}
	//sub减法操作 基本同其他四则运算操作
	private String sub(String instr) {
		int flag1,flag2;
		String str0=explainer(instr);
		String str1=str0.substring(0,str0.indexOf(' '));
		flag1=Integer.parseInt(str1);
		String str2=explainer(str0.substring(str0.indexOf(' ')+1));
		if(str2.indexOf(' ')<0) flag2=Integer.parseInt(str2);
		else flag2=Integer.parseInt(str2.substring(0,str2.indexOf(' ')));
		if(str2.indexOf(' ')>0) return String.valueOf(flag1-flag2)+str2.substring(str2.indexOf(' '));
		return String.valueOf(flag1-flag2);
	}
	//and和操作 基本同其他逻辑操作
	private String and(String instr) {
		int flag1,flag2;
		String str1=explainer(instr);
		if(str1.length()>=3 && str1.charAt(0)=='t' && str1.charAt(1)=='r' && str1.charAt(2)=='u' && str1.charAt(3)=='e') {
			flag1=1;
		}
		else if(str1.length()>=4 && str1.charAt(0)=='f' && str1.charAt(1)=='a' && str1.charAt(2)=='l' && str1.charAt(3)=='s'&& str1.charAt(4)=='e') {
			flag1=0;
		}
		else{
			return "value1 is not bool";
		}
		String str2=explainer(str1.substring(str1.indexOf(' ')+1));
		if(str2.length()>=3 && str2.charAt(0)=='t' && str2.charAt(1)=='r' && str2.charAt(2)=='u' && str2.charAt(3)=='e') {
			flag2=1;
		}
		else if(str2.length()>=4 && str2.charAt(0)=='f' && str2.charAt(1)=='a' && str2.charAt(2)=='l' && str2.charAt(3)=='s'&& str2.charAt(4)=='e') {
			flag2=0;
		}
		else {
			return "value2 is not bool";
		}
		//and操作需要两个变量均为true才会为true
		if(flag1*flag2==1) {
			if(str2.indexOf(' ')>0) {
				return "true"+str2.substring(str2.indexOf(' '));
			}
			else return "true";
		}
		else {
			if(str2.indexOf(' ')>0) {
				return "false"+str2.substring(str2.indexOf(' '));
			}
			else return "false";
		}
	}
	//add加法操作 基本思路同其他四则运算操作
	private String add(String instr) {
		int flag1,flag2;
		String str0=explainer(instr);
		String str1=str0.substring(0,str0.indexOf(' '));
		flag1=Integer.parseInt(str1);
		String str2=explainer(str0.substring(str0.indexOf(' ')+1));
		if(str2.indexOf(' ')<0) flag2=Integer.parseInt(str2);
		else flag2=Integer.parseInt(str2.substring(0,str2.indexOf(' ')));
		if(str2.indexOf(' ')>0) return String.valueOf(flag1+flag2)+str2.substring(str2.indexOf(' '));
		return String.valueOf(flag1+flag2);
	}
	//eq equle等于操作
	private String eq(String instr) {
		String str0=explainer(instr);
		String str1=str0.substring(0,str0.indexOf(' '));
		String str2=explainer(str0.substring(str0.indexOf(' ')+1));
		//找出待分别的两个变量 str1 和 str2的前半部分 直接按照字符串比较方法比较是否相等
		if(str2.indexOf(' ')>0) {
			if(str1.equals(str2.substring(0,str2.indexOf(' ')))) {
				return "true"+str2.substring(str2.indexOf(' '));
			}
			else return "false"+str2.substring(str2.indexOf(' '));
		}
		else {
			if(str1.equals(str2)) {
				return "true";
			}
			else return "false";
		}
		
	}
	
	//make操作 使一个value绑定到一个name上 返回存储的值
	//第二阶段：make定义一个函数
	/*
	 * make <word> [<list1> <list2>]
	 *		word为函数名
	 *		list1为参数列表
	 *		list2为操作列表
	 */
	private String make(String instr) {
		//创建一个新的word变量
		Word newword = new Word();
		//名字为第一个部分的字面量
		newword.name=instr.substring(instr.indexOf('"')+1,instr.indexOf(' '));
		//判断该名字是否存在于命名空间，用到isname函数进行判断
		//如果还没有存在，则进行创建一个新的word 并把它放到命名空间里
		if(isname(instr.substring(instr.indexOf('"'),instr.indexOf(' ')))=="false") {
			//只要不是read和readlist 后面的内容都可以直接和name绑定
			//如果是read 则调用read 把返回内容赋值给name
			if(instr.substring(instr.indexOf(' ')+1).length()>=4 && instr.charAt(instr.indexOf(' ')+1)=='r' && instr.charAt(instr.indexOf(' ')+2)=='e' && instr.charAt(instr.indexOf(' ')+3)=='a' && instr.charAt(instr.indexOf(' ')+4)=='d') {
				newword.value=explainer(instr.substring(instr.indexOf(' ')+1));
			}
			else newword.value=instr.substring(instr.indexOf(' ')+1);
			MUA.wordlist[MUA.point++]=newword;
			return newword.value;
		}
		else {
			//如果是已经存在于命名空间的name 则需要先找到这个name 重新绑定value
			for(int i=0;i<=MUA.point-1;i++) {
				if(MUA.wordlist[i].name.equals(instr.substring(instr.indexOf('"')+1,instr.indexOf(' ')))&&MUA.wordlist[i].value!=null) {
					if(instr.substring(instr.indexOf(' ')+1).length()>=4 && instr.charAt(instr.indexOf(' ')+1)=='r' && instr.charAt(instr.indexOf(' ')+2)=='e' && instr.charAt(instr.indexOf(' ')+3)=='a' && instr.charAt(instr.indexOf(' ')+4)=='d') {
						MUA.wordlist[i].value=explainer(instr.substring(instr.indexOf(' ')+1));
					}
					else MUA.wordlist[i].value=instr.substring(instr.indexOf(' ')+1);
					return null;
				}
			}
			
		}
		return null;
	}
	//取出name绑定的值 可能需要解释器继续解释
	private String thing(String instr) {
		//获得待取的name
		String str0=explainer(instr);
		//在命名空间里查找这个name
		if(str0.indexOf(' ')<0) {
			for(int i=0;i<=MUA.point;i++) {
				if(MUA.wordlist[i].name.equals(str0)) {
					return explainer(MUA.wordlist[i].value);
				}
			}
		}
		else {
			//如果后面还有内容 则把附加的东西也放上去
			for(int i=0;i<=MUA.point;i++) {
				if(MUA.wordlist[i].name.equals(str0.substring(0,str0.indexOf(' ')))) {
					return explainer(MUA.wordlist[i].value+str0.substring(str0.indexOf(' ')));
				}
			}
			return(str0);
		}
		
		return str0;
	}
	//清除name绑定的value 这里因为point的限制没有真正清除 而是把value设置为null
	private String erase(String instr) {
		
		if(instr.charAt(0)!='"') erase(explainer(instr));
		else if(MUA.point>0){
			for(int i=0;i<=MUA.point-1;i++) {
				if(MUA.wordlist[i].name.equals(instr.substring(1))) {
					MUA.wordlist[i].value=null;
					return "Erase succeed";
					//break;
				}
			}
		}
		
		return "No such name";
		//return "ERROR";
	}
	//判断word是否存在于命名空间
	private String isname(String instr) {
		if(instr.charAt(0)!='"') isname(explainer(instr));
		else if(MUA.point>0){
			for(int i=0;i<=MUA.point-1;i++) {
				if(MUA.wordlist[i].name.equals(instr.substring(1))&&MUA.wordlist[i].value!=null) {
					if(instr.indexOf(' ')<0) return "true";
					else return "true"+instr.substring(instr.indexOf(' '));
				}
			}
		}
		if(instr.indexOf(' ')<0) return "false";
		else return "false"+instr.substring(instr.indexOf(' '));
	}
	//print打印 不知道的都丢给解释器
	private void print(String instr) {
		System.out.println(explainer(instr));
	}
	private String read() {
		
		String readword = input.next();
		input.nextLine();
		return readword;
		
	}
	private String readlist() {
		String readalist = input.nextLine();
		return readalist;
	}
	
}

/*test
make "a 6
make "b 2
make "c 6
make "d "abcd
make "e "abcd
make "f "abcde
make "g "abcc
print add :b :a
print sub :b :a
print mod :b :a
print div :b :a
print eq :b :a
print eq :c :a
print eq :d :e
print eq :d :f
print gt :a :b
print gt :b :a
print gt :d :g
print lt :a :b
print lt :b :a
print lt :d :g
print and eq :a :a eq :b :b
print and eq :a :a eq :b :a
print or eq :a :a eq :b :a
print or eq :a :b eq :a :b
print not eq :a :b
*/