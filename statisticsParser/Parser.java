

package statisticsParser;
import org.jfree.data.xy.XYSeries;


public class Parser {
	public static final int _EOF = 0;
	public static final int _number = 1;
	public static final int _account = 2;
	public static final int maxT = 10;

	static final boolean T = true;
	static final boolean x = false;
	static final int minErrDist = 2;

	public Token t;    // last recognized token
	public Token la;   // lookahead token
	int errDist = minErrDist;
	
	public Scanner scanner;
	public Errors errors;
	public XYSeries[] series;

	

	public Parser(Scanner scanner, XYSeries[] series) {
		this.scanner = scanner;
		this.series = series;
		errors = new Errors();
	}

	void SynErr (int n) {
		if (errDist >= minErrDist) errors.SynErr(la.line, la.col, n);
		errDist = 0;
	}

	public void SemErr (String msg) {
		if (errDist >= minErrDist) errors.SemErr(t.line, t.col, msg);
		errDist = 0;
	}
	
	public void add(int oindex, int iindex) {
		XYSeries output = series[oindex];
		XYSeries input = series[iindex];
		int length1 = output.getItemCount();
		int length2 = input.getItemCount();
		
		for(int i = 0; i < Math.min(length1,length2); i++) {
			output.updateByIndex(i,input.getY(i).floatValue() + output.getY(i).floatValue());
		}
		if(length2 > length1) {
			for(int i = length1; i < length2; i++) {
			output.add(input.getX(i).floatValue(),input.getY(i).floatValue());
			}
		}
	}
	
	public void addNA(int number, int index) {
		addAN(index,number);
	}
	
	public void addAN(int index, int number) {
		XYSeries output = series[index];
		int length = output.getItemCount();
		for(int i = 0; i < length; i++) {
			output.updateByIndex(i,output.getY(i).floatValue() + number);
		}
	}
	
	public void subNA(int number, int index) {
	
	}
	
	public void subAN(int index, int number) {
	
	}
	
	public void divNA(int number, int index) {
	
	}
	
	public void divAN(int index, int number) {
	
	}
	
	public void factNA(int number, int index) {
	
	}
	
	public void factAN(int index, int number) {
	
	}
	
	
	
	
	
	public void sub(int oindex, int iindex) {
	XYSeries output = series[oindex];
		XYSeries input = series[iindex];
		int length1 = output.getItemCount();
		int length2 = input.getItemCount();
		
		for(int i = 0; i < Math.min(length1,length2); i++) {
			output.updateByIndex(i,input.getY(i).floatValue() - output.getY(i).floatValue());
		}
		if(length2 > length1) {
			for(int i = length1; i < length2; i++) {
			output.add(input.getX(i).floatValue(),-1*input.getY(i).floatValue());
			}
		}
	}
	public void fact(int oindex, int iindex) {
	XYSeries output = series[oindex];
		XYSeries input = series[iindex];
		int length1 = output.getItemCount();
		int length2 = input.getItemCount();
		
		for(int i = 0; i < Math.min(length1,length2); i++) {
			output.updateByIndex(i,input.getY(i).floatValue() * output.getY(i).floatValue());
		}
		if(length2 > length1) {
			for(int i = length1; i < length2; i++) {
			output.add(input.getX(i).floatValue(),0.0);
			}
		}
	}
	public void div(int oindex, int iindex) {
	XYSeries output = series[oindex];
		XYSeries input = series[iindex];
		int length1 = output.getItemCount();
		int length2 = input.getItemCount();
		
		for(int i = 0; i < Math.min(length1,length2); i++) {
			output.updateByIndex(i,input.getY(i).floatValue() / output.getY(i).floatValue());
		}
		/*
		if(length2 > length1) {
			for(int i = length1; i < length2; i++) {
			output.add(input.getX(i).floatValue(),input.getY(i).floatValue());
			}
		}
		*/
	}
	
	private int parseAcc(String val) {
		return Integer.parseInt(val.substring(2));
	}
	
	void Get () {
		for (;;) {
			t = la;
			la = scanner.Scan();
			if (la.kind <= maxT) {
				++errDist;
				break;
			}

			la = t;
		}
	}
	
	void Expect (int n) {
		if (la.kind==n) Get(); else { SynErr(n); }
	}
	
	boolean StartOf (int s) {
		return set[s][la.kind];
	}
	
	void ExpectWeak (int n, int follow) {
		if (la.kind == n) Get();
		else {
			SynErr(n);
			while (!StartOf(follow)) Get();
		}
	}
	
	boolean WeakSeparator (int n, int syFol, int repFol) {
		int kind = la.kind;
		if (kind == n) { Get(); return true; }
		else if (StartOf(repFol)) return false;
		else {
			SynErr(n);
			while (!(set[syFol][kind] || set[repFol][kind] || set[0][kind])) {
				Get();
				kind = la.kind;
			}
			return StartOf(syFol);
		}
	}
	
	void Sample() {
		int n; 
		while (la.kind == 3) {
			Get();
			n = Add();
			System.out.println("heehee"); 
		}
	}

	int  Add() {
		int  i;
		int j = 0;i=0; 
		if (la.kind == 1 || la.kind == 2 || la.kind == 8) {
			i = Sub();
			while (la.kind == 4) {
				Get();
				j = Sub();
				add(i,j); 
			}
		} else if (la.kind == 1 || la.kind == 2 || la.kind == 8) {
			i = Sub();
			while (la.kind == 4) {
				Get();
				j = SubN();
				addAN(i,j); 
			}
		} else if (la.kind == 1 || la.kind == 8) {
			i = SubN();
			while (la.kind == 4) {
				Get();
				j = Sub();
				addNA(i,j); 
			}
		} else SynErr(11);
		return i;
	}

	int  Sub() {
		int  i;
		int j = 0;i=0; 
		if (la.kind == 1 || la.kind == 2 || la.kind == 8) {
			i = Fact();
			while (la.kind == 5) {
				Get();
				j = Fact();
				sub(i,j); 
			}
		} else if (la.kind == 1 || la.kind == 2 || la.kind == 8) {
			i = Fact();
			while (la.kind == 5) {
				Get();
				j = FactN();
				subAN(i,j); 
			}
		} else if (la.kind == 1 || la.kind == 8) {
			i = FactN();
			while (la.kind == 5) {
				Get();
				j = Fact();
				subNA(i,j); 
			}
		} else SynErr(12);
		return i;
	}

	int  SubN() {
		int  i;
		int j; 
		i = FactN();
		while (la.kind == 5) {
			Get();
			j = FactN();
			i = i - j; 
		}
		return i;
	}

	int  Fact() {
		int  i;
		int j = 0;i=0; 
		if (la.kind == 1 || la.kind == 2 || la.kind == 8) {
			i = Div();
			while (la.kind == 6) {
				Get();
				j = Div();
				fact(i,j); 
			}
		} else if (la.kind == 1 || la.kind == 2 || la.kind == 8) {
			i = Div();
			while (la.kind == 6) {
				Get();
				j = DivN();
				factAN(i,j); 
			}
		} else if (la.kind == 1 || la.kind == 8) {
			i = DivN();
			while (la.kind == 6) {
				Get();
				j = Div();
				factNA(i,j); 
			}
		} else SynErr(13);
		return i;
	}

	int  FactN() {
		int  i;
		int j; 
		i = DivN();
		while (la.kind == 6) {
			Get();
			j = DivN();
			i = i*j; 
		}
		return i;
	}

	int  Div() {
		int  i;
		int j = 0; i = 0; 
		if (la.kind == 2 || la.kind == 8) {
			i = Bracket();
			while (la.kind == 7) {
				Get();
				j = Bracket();
				div(i,j); 
			}
		} else if (la.kind == 2 || la.kind == 8) {
			i = Bracket();
			while (la.kind == 7) {
				Get();
				j = BracketN();
				divAN(i,j); 
			}
		} else if (la.kind == 1 || la.kind == 8) {
			i = BracketN();
			while (la.kind == 6) {
				Get();
				j = Bracket();
				divNA(i,j); 
			}
		} else SynErr(14);
		return i;
	}

	int  DivN() {
		int  i;
		int j; 
		i = BracketN();
		while (la.kind == 7) {
			Get();
			j = BracketN();
			i = i/j; 
		}
		return i;
	}

	int  Bracket() {
		int  i;
		i = 0; 
		if (la.kind == 2) {
			Get();
			i = parseAcc(t.val); 
		} else if (la.kind == 8) {
			Get();
			i = Add();
			Expect(9);
		} else SynErr(15);
		return i;
	}

	int  BracketN() {
		int  i;
		i = 0; 
		if (la.kind == 1) {
			Get();
			i = Integer.parseInt(t.val); 
		} else if (la.kind == 8) {
			Get();
			i = AddN();
			Expect(9);
		} else SynErr(16);
		return i;
	}

	int  AddN() {
		int  i;
		int j; 
		i = SubN();
		while (la.kind == 4) {
			Get();
			j = SubN();
			i = i + j; 
		}
		return i;
	}



	public void Parse() {
		la = new Token();
		la.val = "";		
		Get();
		Sample();

		Expect(0);
	}

	private static final boolean[][] set = {
		{T,x,x,x, x,x,x,x, x,x,x,x}

	};
} // end Parser


class Errors {
	public int count = 0;                                    // number of errors detected
	public java.io.PrintStream errorStream = System.out;     // error messages go to this stream
	public String errMsgFormat = "-- line {0} col {1}: {2}"; // 0=line, 1=column, 2=text
	
	protected void printMsg(int line, int column, String msg) {
		StringBuffer b = new StringBuffer(errMsgFormat);
		int pos = b.indexOf("{0}");
		if (pos >= 0) { b.delete(pos, pos+3); b.insert(pos, line); }
		pos = b.indexOf("{1}");
		if (pos >= 0) { b.delete(pos, pos+3); b.insert(pos, column); }
		pos = b.indexOf("{2}");
		if (pos >= 0) b.replace(pos, pos+3, msg);
		errorStream.println(b.toString());
	}
	
	public void SynErr (int line, int col, int n) {
		String s;
		switch (n) {
			case 0: s = "EOF expected"; break;
			case 1: s = "number expected"; break;
			case 2: s = "account expected"; break;
			case 3: s = "\"exp\" expected"; break;
			case 4: s = "\"+\" expected"; break;
			case 5: s = "\"-\" expected"; break;
			case 6: s = "\"*\" expected"; break;
			case 7: s = "\"/\" expected"; break;
			case 8: s = "\"(\" expected"; break;
			case 9: s = "\")\" expected"; break;
			case 10: s = "??? expected"; break;
			case 11: s = "invalid Add"; break;
			case 12: s = "invalid Sub"; break;
			case 13: s = "invalid Fact"; break;
			case 14: s = "invalid Div"; break;
			case 15: s = "invalid Bracket"; break;
			case 16: s = "invalid BracketN"; break;
			default: s = "error " + n; break;
		}
		printMsg(line, col, s);
		count++;
	}

	public void SemErr (int line, int col, String s) {	
		printMsg(line, col, s);
		count++;
	}
	
	public void SemErr (String s) {
		errorStream.println(s);
		count++;
	}
	
	public void Warning (int line, int col, String s) {	
		printMsg(line, col, s);
	}
	
	public void Warning (String s) {
		errorStream.println(s);
	}
} // Errors


class FatalError extends RuntimeException {
	public static final long serialVersionUID = 1L;
	public FatalError(String s) { super(s); }
}

