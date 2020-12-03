
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;



public class Automate {
	private int initiale; // l’état initial
	private List<String[]> tab; // l’ensemble des instructions (Représentation matricielle)
	private List<String> alpha; // l’alphabet d’entrée
	private List<Integer> finale; // l’ensemble des états finaux
	public Automate(int init,List<String[]> tab,List<String> alpha,List<Integer> finale) {
		this.tab = tab;
		this.alpha = alpha;
		this.finale = finale;
		initiale=init;	
	}
	public List<String[]> reduit() {
		// La réduction d’un automate
		// éliminantion des états non accessibles et non co-accessibles
		List<String[]> newtab = new ArrayList<String[]>();
		boolean stop=false;
		List<Integer> visited = new ArrayList<Integer>();
		List<Integer> acc = new ArrayList<Integer>();
		acc.add(this.initiale);
		int k=acc.get(0);
		while(!stop) {
		String[] row=tab.get(k);
	        for(int i=0;i<row.length;i++) {
	        	if(!acc.contains(i) && !row[i].isEmpty()) acc.add(i);
	        }
		visited.add(k);
		stop=true;
		for(Integer etat : acc) {
			if(!visited.contains(etat)) {
				k=etat;
				stop=false;
				break;
			}
		}
		}
		List<Integer> coacc = new ArrayList<Integer>();
		visited=new ArrayList<Integer>();
		stop=false;
		coacc.addAll(finale);
        k=coacc.get(0);
        int i=0;
		while(!stop) {
			i=0;
		        for(String[] row : tab) {
		        	if(!coacc.contains(i) && !row[k].isEmpty()) coacc.add(i);
		        	i++;
		        }
			visited.add(k);
			stop=true;
			for(Integer etat : coacc) {
				if(!visited.contains(etat)) {
					k=etat;
					stop=false;
					break;
				}
			}
			}
		acc.retainAll(coacc);
		int j=0;
		String[] newrow;
		for (String[] row : tab) {
	        if(acc.contains(j)) {
	        	int i1=0;
	        	newrow=new String[acc.size()];
	        	for(i=0;i<row.length;i++) {
	        		if(acc.contains(i)) {
	        			newrow[i1]=row[i];
	        			i1++;
	        		}
	        	}
	        	newtab.add(newrow);
	        }
	        j++;
	    }
		return newtab;
	}
	public boolean mot(String s) {
		// La reconnaissance de mots dans un automate déterministe
		// Retourne vrai si le mot est reconnu par l'automate
		int i=0;
		int etat=this.initiale;
		while(i<s.length()){
			if(!this.alpha.contains(Character.toString(s.charAt(i)))) return false;
			String[] row=this.tab.get(etat);
			int j;
			for(j=0;j<row.length;j++) {
				if(row[j].contains(Character.toString(s.charAt(i))) || row[j].contains("_") ) {
					etat=j;
					break;
				}
			}
			if(this.finale.contains(etat) && i==s.length()-1) return true;
			if(j==row.length) return false;
			i++;
		}
		if(s.length()==0 && this.finale.contains(this.initiale)) return true;
		return false;
	}
	public Automate miroir(){
		// Le miroir d’un automate
		List<String[]> newtab=new ArrayList<String[]>();
		int j=0,r=-1;
		if(this.finale.size()>1) {
		for (String[] row : this.tab) {
			r=row.length;
	        String[] newrow=new String[row.length+1];
	        for(int i=0;i<row.length;i++) {
	        	newrow[i]="";
	        }
	        newrow[row.length]="";
	        newtab.add(newrow);
	    j++;
	    }}
		String[] lastrow=new String[r+1];
		for(int i=0;i<r+1;i++) {
			if(this.finale.contains(i)) {
				lastrow[i]="_";
			}else {
				lastrow[i]="";
			}
			
		}
		newtab.add(lastrow);
		j=0;
		for (String[] row : tab) {
			for(int i=0;i<row.length;i++) {
				if(!row[i].isEmpty()) {
					newtab.get(i)[j]=row[i];
				}
			}
	    j++;
	    }
		List<Integer> fin=new ArrayList<Integer>();
		fin.add(this.initiale);
		Automate n=new Automate(r,newtab,this.alpha,fin);
		return n;
		
	}
	
	public Automate complement() {
		// Le complément d’un automate
		//this.det(); // TO-DO check edge cases for det before calling it
		List<String[]> newtab=new ArrayList<String[]>();
		int j=0,r=-1;
		List<String> miss;
		String need=new String(); 
		for (String[] row : this.tab) {
			need="";
			for(String s:this.alpha) {
				need=need.concat(s);
			}
			r=row.length;
	        String[] newrow=new String[row.length+1];
	        for(int i=0;i<row.length;i++) {
	        	newrow[i]=row[i];
	        	for(int k=0;k<row[i].length();k++) {
	        		need=deleteCharAt(need,need.indexOf(row[i].charAt(k)));
	        	}
	        }
	        if(need.length()>0) {
	        	newrow[r]=need;
	        }else {
	        	newrow[r]="";
	        }
	        newtab.add(newrow);
	    j++;
	    }
		String[] lastrow=new String[r+1];
		for(int i=0;i<r;i++) {
				lastrow[i]="";
		}
		need="";
		for(String s:this.alpha) {
			need=need.concat(s);
		}
		lastrow[r]=need;
		newtab.add(lastrow);
		List<Integer> fin=new ArrayList<Integer>();
		for(int i=0;i<newtab.size();i++) {
			if(!this.finale.contains(i)) fin.add(i);
		}
		for (String[] row : newtab) {
	        System.out.println("Row = " + Arrays.toString(row));
	    }
		Automate n=new Automate(this.initiale,newtab,this.alpha,fin);
		Automate m=new Automate(this.initiale,n.reduit(),this.alpha,fin);
		return m;
	}
	private static String deleteCharAt(String strValue, int index) {
		if(index<0) return strValue;
		return strValue.substring(0, index) + strValue.substring(index + 1);

	}
	public void  det() {
		// Le passage d’un automate non déterministe à un automate déterministe   
		boolean stop=false,prob;
		HashMap<String,Integer> map= new HashMap<String,Integer>();
		for(String s: this.alpha) {
			map.put(s, 0);
		}
		while(!stop) {
			int k=0;
			stop=true;
			for(String[] row :this.tab) {
				prob=false;
				for(int i=0;i<row.length;i++) {
					if(!row[i].isEmpty()) {
						for(int j=0;j<row[i].length();j++) {
							map.put(Character.toString(row[i].charAt(j)), map.get(Character.toString(row[i].charAt(j))+1));
							if(map.get(Character.toString(row[i].charAt(j)))>1) {
								int rowat=k;
								int colat=i;
								stop=false;
								String c=Character.toString(row[i].charAt(j));
								List<Integer> l=new ArrayList<Integer>();
								for(int i1=0;i1<row.length;i1++) {
									if(row[i1].contains(c)) {
										l.add(i1);
										//row[i1]=deleteCharAt(row[i1],row[i1].indexOf(c));
									}
								}
								String[] lastrow=new String[row.length+1];
								for(int c1=0;c1<lastrow.length;c1++) {
									lastrow[c1]="";
								}
								for(int k1=0;k1<tab.size();k1++) {
									String[] row1=tab.get(k1);
									String[] newrow=new String[row1.length+1];
									if(k1==rowat) {
										for(int c1=0;c1<row1.length;c1++) {
											if(row1[c1].contains(c)) {
												newrow[c1]=deleteCharAt(row1[c1],row1[c1].indexOf(c));
											}else {
												newrow[c1]=row1[c1];
											}
										}
										newrow[row1.length]=c;
									}
									for(int c1=0;c1<row1.length;c1++) {
										newrow[c1]=row[c1];
									}
									newrow[row.length]="";
									if(l.contains(k1)) {
										for(int c1=0;c1<row1.length;c1++) {
											newrow[c1]=row[c1];
											if(!lastrow[c1].contains(row1[c1])){
												lastrow[c1].concat(row1[c1]);
											}
										}
									}
								tab.add(k1, newrow);
								}
								tab.add(lastrow);
							}
						}
					}
				}
			k++;
			}
		}
		}
	public static void main (String args[]){
		// ici vous pouvez définir votre propre automate et tester les fonctionalités
		int initiale=0;
		List<String[]> tabl=new ArrayList<String[]>();
		tabl.add(new String[] {"","ab","","b","",""});
		tabl.add(new String[] {"","","a","","",""});
		tabl.add(new String[] {"a","","","","",""});
		tabl.add(new String[] {"","","","","",""});
		tabl.add(new String[] {"","","","","","b"});
		tabl.add(new String[] {"","","","","a",""});
		List<String> alpha = new ArrayList<String>();
		alpha.add("a");
		alpha.add("b");
		List<Integer> finale=new ArrayList<Integer>();
		finale.add(2);
		finale.add(1);
		Automate m=new Automate(initiale,tabl,alpha,finale);
		//test reduit
		
		m=new Automate(initiale,m.reduit(),alpha,finale);
		for (String[] row : m.tab) {
	        System.out.println("Row = " + Arrays.toString(row));
	    }
		// test lecture d'un mot
		
		System.out.println("aaab :"+m.mot("aaab"));
		System.out.println("aabab :"+m.mot("aabab"));
		
		//test mirroir
		Automate n =m.miroir();
		for (String[] row : n.tab) {
	        System.out.println("Row = " + Arrays.toString(row));
	    }
		//test complement
		n=m.complement();
		System.out.println("aaab :"+n.mot("aaab"));
		System.out.println("aabab :"+n.mot("aabab"));
		for (String[] row : n.tab) {
	        System.out.println("Row = " + Arrays.toString(row));
	    }
	}

}

