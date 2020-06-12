import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
public class T11_37_4158_Ahmed_Hossam {

 
	//T11_37_4158_Ahmed_Hossam
    public static void main(String[] args) {

    	String grammar  = "S,iST,e;T,cS,a";
    	String input1 = "iiac";
    	String input2 = "iia";
    	CFG g = new CFG(grammar);
    	System.out.println(g.table());
    	System.out.println(g.parse(input1));
    	System.out.println(g.parse(input2));
 
    }
    public static class CFG {

        private String startingVariable;
        private Set<String> variables;
        private Set<String> terminals;
        private Map<String, ArrayList<String>> rules;
        private Map<String, Set<String>> firstFinal;
        private Map<String, Set<String>> followFinal;
        private String[][] table;
        public CFG(String input) {
        	variables = new HashSet<>();
        	terminals = new HashSet<>();
        	rules = new LinkedHashMap<>();
        	this.startingVariable = input.charAt(0)+"";
        	String[] splitted = input.split(";");
        	for(String rules:splitted) {
        		for(int i = 2;i<rules.length();i++) {
        			if(Character.isLowerCase(rules.charAt(i))) {
        				if(rules.charAt(i)=='e') {
        					this.terminals.add("$");
        				}
        				else {
        					this.terminals.add(rules.charAt(i)+"");
        				}
        			}
        		}
        		this.variables.add(rules.charAt(0)+"");
        		String[] rule = rules.split(",");
        		ArrayList<String> list = new ArrayList<>();
        		for(int i = 1;i<rule.length;i++) {
        			if(rule[i].contains("e")) {
        				list.add("$");
        			}
        			else {
            			list.add(rule[i]);
        			}
        		}
        		this.rules.put(rule[0], list);
        	}
        	table = new String[this.variables.size()+1][this.terminals.size()+1];
        	table[0][0] = "non";
        	ArrayList<String> terms = new ArrayList<String>();
        	ArrayList<String> var = new ArrayList<String>();
        	boolean feEpsilon = false;
        	for(String s: this.terminals) {
        		if(!s.equals("$")) {
        			terms.add(s);
        			feEpsilon = true;
        		}
        		
        	}
        	if(feEpsilon) {
        		terms.add("$");
        	}
        	for(String s: this.variables) {
        		var.add(s);
        	}
        	for(int i = 0;i<table.length;i++) {
        		for(int j = 0;j<table[i].length;j++) {
        			table[i][j] = "";
        		}
        	}
        	for(int j = 1;j<table[0].length;j++) {
        		table[0][j] = terms.get(j-1);
        	}
        	for(int i = 1;i<table.length;i++) {
        		table[i][0] = var.get(i-1);
        	}
        	
        }
        public CFG(String startingVariable, Set<String> variables, Set<String> terminals, Map<String, ArrayList<String>> rules) {
            this.startingVariable = startingVariable;
            this.variables = variables;
            this.terminals = terminals;
            this.rules = rules;
        }
        public static final String EPSILON = "$";
        
        public String table() {
        	this.First();
        	this.Follow();
        	   for (Map.Entry<String, ArrayList<String>> entry : this.rules.entrySet()) {
                   for (String production : entry.getValue()) {
                	   
              		 	Object[] first = firstFinal.get(production.charAt(0)+"").toArray();
              		 	Object[] follow = followFinal.get(entry.getKey()).toArray();
              		 	if(!production.equals("$")) {
              		 		for(int i = 0;i<first.length;i++) {
              		 			for(int j = 1;j<table.length;j++) {
              		 				if(table[j][0].equals(entry.getKey())) {
              		 					for(int x = 1;x<table[j].length;x++) {
              		 						if(first[i].equals(table[0][x])) {
              		 							table[j][x]+=production;
              		 						}
              		 					}
              		 				}
              		 			}
                  		 	}
                 	   }
              		 	else {
              		 		for(int i = 0;i<follow.length;i++) {
              		 			for(int j = 1;j<table.length;j++) {
              		 				if(table[j][0].equals(entry.getKey())) {
              		 					for(int x = 1;x<table[j].length;x++) {
              		 						if(follow[i].equals(table[0][x])) {
              		 							table[j][x]+=production;
              		 						}
              		 					}
              		 				}
              		 			}
                  		 	}
              		 	}
              		 	
                	   
                   }
        	   }
        	   for(int i = 1;i<table.length;i++) {
          		 for(int j = 1;j<table[i].length;j++) {
          			 if(table[i][j].equals("$")) {
          				 table[i][j] = "e";
          			 }
          		 }
          	 }
        	 String finalString = "";
        	 for(int i = 1;i<table.length;i++) {
        		 for(int j = 1;j<table[i].length;j++) {
        			 if(table[i][j].length()!=0) {
                		 finalString+=table[i][0]+",";
            			 finalString+=table[0][j]+","+table[i][j]+";";
        			 }
        		 }
        	 }
			return finalString;
		}
        public String parse(String s) {
        	for(int i = 1;i<table.length;i++) {
         		 for(int j = 1;j<table[i].length;j++) {
         			 if(table[i][j].equals("e")) {
         				 table[i][j] = "$";
         			 }
         		 }
         	 }
        	s +="$";
        	String string = this.startingVariable;
        	String cumulative = string;
        	String temp = string;
        	Stack<String> st = new Stack<String>();
        	st.push("$");
        	st.push(this.startingVariable);
        	int ip = 0;
        	while(!st.isEmpty()) {
        		if(!string.equals(temp)) {
        			temp = string;
        			cumulative += ","+string;
        		}
        		String currentChar = s.charAt(ip)+"";
        		if(currentChar.equals(st.peek())) {
        			st.pop();
        			ip++;
        		}
        		else {
        			if(this.variables.contains(st.peek())) {
        				boolean changed = false;
        				for(int i =1;i<table.length;i++) {
        					if(st.peek().equals(table[i][0])) {
        						if(changed) {
        							break;
        						}
        						for(int j = 1;j<table[i].length;j++) {
        							if(currentChar.equals(table[0][j])) {
        								if(table[i][j].length()>0) {
        									if(table[i][j].contains("$")) {
        										string=string.replaceFirst(st.pop(), "");
        										changed = true;
        										break;
        									}
        									else {
        										string=string.replaceFirst(st.pop(), table[i][j]);
        										
        										for(int x = table[i][j].length()-1;x>=0;x--) {
                									st.push(table[i][j].charAt(x)+"");
                								}
        										changed = true;
        										break;
        									}
        									
        								}
        								else {
        									string = "ERROR";
        									break;
        								}
        								
        							}
        						}
        					}
        				}
        			}
        			else{
        				string = "ERROR";
						break;
        			}
        		}
        		if(string.equals("ERROR")) {
            		cumulative+=","+string;
            		break;
            	}
        	}
        	
			return cumulative;
		}
        private boolean isSubset(Set<String> setA, Set<String> setB) {
            if (setA == null || setB == null) {
                return false;
            }
            boolean aHasEps = false;
            boolean bHasEps = false;
            boolean returnValue;
            if (!setA.isEmpty() && setA.contains(EPSILON)) {
                aHasEps = true;
            } else {
                setA.add(EPSILON);
            }
            if (!setB.isEmpty() && setB.contains(EPSILON)) {
            	bHasEps = true;
            } else {
                setB.add(EPSILON);
            }
            if (setB.containsAll(setA)) {
                returnValue = true;
            } else
                returnValue = false;
            if (!aHasEps) {
                setA.remove(EPSILON);
            }
            if (!bHasEps) {
                setB.remove(EPSILON);
            }
            return returnValue;
        }

        private String[] parser(String production) {
            return production.split("");
        }

        public  String First() {
            Map<String, Set<String>> first = new LinkedHashMap<>();
            for (String t : this.terminals) {
                first.put(t, new HashSet<>(Collections.singletonList(t)));
            }
            for (String v : this.variables) {
                first.put(v, new HashSet<>(Collections.emptyList()));
            }

            boolean change = true;
            String[] productionArray;

            while (change) {
                change = false;
                for (Map.Entry<String, ArrayList<String>> entry : this.rules.entrySet()) {
                    for (String production : entry.getValue()) {
                        productionArray = parser(production);
                        boolean isEpsilon=true;
                        int index = 0;
                        for(int i = 0;i<productionArray.length;i++) {
                        	if(!first.get(productionArray[i]).contains(EPSILON)) {
                        		isEpsilon = false;
                        		index = i;
                        		break;
                        	}
                        }
                        if(isEpsilon) {
                        	if(!first.get(entry.getKey()).contains(EPSILON)) {
                        		first.get(entry.getKey()).add(EPSILON);
                        		change = true;
                        	}
                        }
                      
                        if (productionArray[0].equals(entry.getKey())) {
                            for (int i = 0; i < productionArray.length - 1; i++) {
                                if (first.get(productionArray[i]).contains(EPSILON)) {
                                    first.get(entry.getKey()).addAll(first.get(productionArray[i + 1]));
                                }
                            }
                        }
                        if(productionArray.length>1) {
                        	if (first.get(productionArray[0]).contains(EPSILON)) {
                        		Set<String> xFX = new HashSet<String>(first.get(productionArray[1]));
                        		xFX.remove("$");
                        		Map<String, Set<String>> tempWithNoEps = new LinkedHashMap<>();
                                tempWithNoEps.put(productionArray[1], new HashSet<>(Collections.emptyList()));
                                tempWithNoEps.get(productionArray[1]).addAll(xFX);
                        		first.get(entry.getKey()).addAll(tempWithNoEps.get(productionArray[1]));
                        	}
                        }
                           
                        
             	  		Set<String> xFX = new HashSet<String>(first.get(productionArray[0]));
                		xFX.remove("$");
                		Map<String, Set<String>> tempWithNoEps = new LinkedHashMap<>();
                        tempWithNoEps.put(productionArray[0], new HashSet<>(Collections.emptyList()));
                        tempWithNoEps.get(productionArray[0]).addAll(xFX);
                        if (!tempWithNoEps.get(productionArray[0]).isEmpty() && !isSubset(tempWithNoEps.get(productionArray[0]), first.get(entry.getKey()))) {
                        	first.get(entry.getKey()).addAll(tempWithNoEps.get(productionArray[0]));
                        	if(first.get(productionArray[0]).contains(EPSILON)) {
                        		
                        		for(int i = 1;i<productionArray.length;i++) {
                        	  		Set<String> xFY= new HashSet<String>(first.get(productionArray[i]));
                            		xFY.remove("$");
                            		Map<String, Set<String>> tempWithNoEps2 = new LinkedHashMap<>();
                                    tempWithNoEps2.put(productionArray[i], new HashSet<>(Collections.emptyList()));
                                    tempWithNoEps2.get(productionArray[i]).addAll(xFY);
                        			if(first.get(productionArray[i]).contains(EPSILON)) {
                        				first.get(entry.getKey()).addAll(tempWithNoEps2.get(productionArray[i]));
                        			}
                        			else {
                        				first.get(entry.getKey()).addAll(tempWithNoEps2.get(productionArray[i]));
                        				break;
                        			}
                        			
                        		}
//                        		if(productionArray.length>1) {
//                            		first.get(entry.getKey()).addAll(first.get(productionArray[1]));
//                        		}
                        	}
                            change = true;
                        }
//                        for(int i = 0;i<productionArray.length;i++) {
//                        	if(i==0||isEpsilon) {
//                        		Set<String> xFX = new HashSet<String>(first.get(productionArray[i]));
//                        		xFX.remove("$");
//                        		Map<String, Set<String>> tempWithNoEps = new LinkedHashMap<>();
//                                tempWithNoEps.put(productionArray[i], new HashSet<>(Collections.emptyList()));
//                                tempWithNoEps.get(productionArray[i]).addAll(xFX);
//                                System.out.println(tempWithNoEps);
//                        		if (!tempWithNoEps.get(productionArray[i]).isEmpty() && !isSubset(tempWithNoEps.get(productionArray[i]), first.get(entry.getKey()))) {
//                        			first.get(entry.getKey()).addAll(tempWithNoEps.get(productionArray[i]));
//                                    change = true;
//                                }
//                        	}
//                        	
//                        		
//                            
//                        }
                        
                      
                    }

                }
            }
            firstFinal =  new LinkedHashMap<>(first);
            return encodeFirst(first);
        }
//        public  String Follow() {
//       	 Map<String, Set<String>> first = new LinkedHashMap<>(firstFinal);
//            Map<String, Set<String>> follow = new LinkedHashMap<>();
//            for (String v : this.variables) {
//                if (v.equals(this.startingVariable)) {
//                    follow.put(v, new HashSet<>(Collections.singleton(EPSILON)));
//                } else
//                    follow.put(v, new HashSet<>(Collections.emptyList()));
//            }
//            for (String t : this.terminals) {
//                follow.put(t, new HashSet<>(Collections.emptyList()));
//            }
//            boolean change = true;
//            while(change) {
//           	 change = false;
//           	 String[] productionArray;
//           	 for (Map.Entry<String, ArrayList<String>> entry : this.rules.entrySet()) {
//           	     for (String production : entry.getValue()) {
//                        productionArray = parser(production);
//                        if (this.variables.contains(productionArray[productionArray.length - 1])) {
//                            follow.get(productionArray[productionArray.length - 1]).addAll(follow.get(entry.getKey()));
//                        }
//                        if (productionArray.length >= 2) {
//                            for(int i = 0;i<productionArray.length-1;i++) {
//                           		Set<String> xFY= new HashSet<String>(first.get(productionArray[i+1]));
//                           		xFY.remove("$");
//                           		Map<String, Set<String>> tempWithNoEps2 = new LinkedHashMap<>();
//                                   tempWithNoEps2.put(productionArray[i+1], new HashSet<>(Collections.emptyList()));
//                                   tempWithNoEps2.get(productionArray[i+1]).addAll(xFY);
//                           	 if (!isSubset(first.get(productionArray[i+1]), follow.get(productionArray[i]))) {
//                                    follow.get(productionArray[i]).addAll(tempWithNoEps2.get(productionArray[i + 1]));
//                                    change = true;
//                                }
//                              
//                            }
//                            for(int i = 0;i<productionArray.length;i++) {
//                       		 if(i+1<productionArray.length)
//                           	 if (first.get(productionArray[i + 1]).contains(EPSILON)) {
//                           		 for(int j = i+1;j<productionArray.length;j++) {
//                               		 if(j+1<productionArray.length) {
//                               			 Set<String> xFY= new HashSet<String>(first.get(productionArray[j+1]));
//                                     		xFY.remove("$");
//                                     		Map<String, Set<String>> tempWithNoEps2 = new LinkedHashMap<>();
//                                             tempWithNoEps2.put(productionArray[j+1], new HashSet<>(Collections.emptyList()));
//                                             tempWithNoEps2.get(productionArray[j+1]).addAll(xFY);
//                                			 if (first.get(productionArray[j + 1]).contains(EPSILON)) {
//                                				 if(j+1 == productionArray.length-1) {
//                                                     follow.get(productionArray[i]).addAll(first.get(productionArray[j + 1]));
//                                				 }
//                                				 else {
//                                                     follow.get(productionArray[i]).addAll(tempWithNoEps2.get(productionArray[j + 1]));
//                                				 }
//                                			 }
//                                			 else {
//                                                follow.get(productionArray[i]).addAll(tempWithNoEps2.get(productionArray[j + 1]));
//                                				 break;
//                                			 }
//                               		 }
//                               		 else {
//                               			 Set<String> xFY= new HashSet<String>(first.get(productionArray[i+1]));
//                                      		xFY.remove("$");
//                                      		Map<String, Set<String>> tempWithNoEps2 = new LinkedHashMap<>();
//                                              tempWithNoEps2.put(productionArray[i+1], new HashSet<>(Collections.emptyList()));
//                                              tempWithNoEps2.get(productionArray[i+1]).addAll(xFY);
//                                            follow.get(productionArray[i]).addAll(tempWithNoEps2.get(productionArray[i + 1]));
//                               		 }
//                           
//                           		 }
//                           	 }
//                           	 
//                            }
//                            for(int i = 0;i<productionArray.length-1;i++) {
//                                if (first.get(productionArray[i + 1]).contains(EPSILON)) {
//   	                        	 if (!isSubset(follow.get(entry.getKey()), follow.get(productionArray[i]))) {
//   	                                 follow.get(productionArray[i]).addAll(follow.get(entry.getKey()));
//   	                                 change = true;
//   	
//   	                             }
//   	                        	if(i==0&&i+2<productionArray.length) {
//   	                        		 follow.get(productionArray[i]).addAll(first.get(productionArray[i + 2]));
//   	                        	}
//                                }
//                                
//                            }
//                           
//                        }
//                        else {
//                            follow.get(productionArray[0]).addAll(follow.get(entry.getKey()));
//                        }
//                      
//           	     }
//           	 }
//            }
//            return encodeFollow(follow);
//       }
        public  String Follow() {
        	 Map<String, Set<String>> first = new LinkedHashMap<>(firstFinal);
             Map<String, Set<String>> follow = new LinkedHashMap<>();
             for (String v : this.variables) {
                 if (v.equals(this.startingVariable)) {
                     follow.put(v, new HashSet<>(Collections.singleton(EPSILON)));
                 } else
                     follow.put(v, new HashSet<>(Collections.emptyList()));
             }
             for (String t : this.terminals) {
                 follow.put(t, new HashSet<>(Collections.emptyList()));
             }
             boolean change = true;
             while(change) {
            	 change = false;
            	 String[] productionArray;
            	 for (Map.Entry<String, ArrayList<String>> entry : this.rules.entrySet()) {
            	     for (String production : entry.getValue()) {
                         productionArray = parser(production);
                         if (this.variables.contains(productionArray[productionArray.length - 1])) {
                             follow.get(productionArray[productionArray.length - 1]).addAll(follow.get(entry.getKey()));
                         }
                         if (productionArray.length >= 2) {
                             for(int i = 0;i<productionArray.length-1;i++) {
                            		Set<String> xFY= new HashSet<String>(first.get(productionArray[i+1]));
                            		xFY.remove("$");
                            		Map<String, Set<String>> tempWithNoEps2 = new LinkedHashMap<>();
                                    tempWithNoEps2.put(productionArray[i+1], new HashSet<>(Collections.emptyList()));
                                    tempWithNoEps2.get(productionArray[i+1]).addAll(xFY);
                            	 if (!isSubset(first.get(productionArray[i+1]), follow.get(productionArray[i]))) {
                                     follow.get(productionArray[i]).addAll(tempWithNoEps2.get(productionArray[i + 1]));
                                     change = true;
                                 }
                               
                             }
                             for(int i = 0;i<productionArray.length;i++) {
                        		 if(i+1<productionArray.length) {
                            	 if (first.get(productionArray[i + 1]).contains(EPSILON)) {
                            		 for(int j = i+1;j<productionArray.length;j++) {
                                		 if(j+1<productionArray.length) {
                                			 Set<String> xFY= new HashSet<String>(first.get(productionArray[j+1]));
                                      		xFY.remove("$");
                                      		Map<String, Set<String>> tempWithNoEps2 = new LinkedHashMap<>();
                                              tempWithNoEps2.put(productionArray[j+1], new HashSet<>(Collections.emptyList()));
                                              tempWithNoEps2.get(productionArray[j+1]).addAll(xFY);
                                 			 if (first.get(productionArray[j + 1]).contains(EPSILON)) {
                                 				 if(j+1 == productionArray.length-1) {
                                                      follow.get(productionArray[i]).addAll(first.get(productionArray[j + 1]));
                                 				 }
                                 				 else {
                                                      follow.get(productionArray[i]).addAll(tempWithNoEps2.get(productionArray[j + 1]));
                                 				 }
                                 			 }
                                 			 else {
                                                 follow.get(productionArray[i]).addAll(tempWithNoEps2.get(productionArray[j + 1]));
                                 				 break;
                                 			 }
                                		 }
                                		 else {
                                			 if(i+1==productionArray.length-1) {
                                                 follow.get(productionArray[i]).addAll(follow.get(entry.getKey()));

                                			 }
                                				 Set<String> xFY= new HashSet<String>(first.get(productionArray[i+1]));
                                            		xFY.remove("$");
                                            		Map<String, Set<String>> tempWithNoEps2 = new LinkedHashMap<>();
                                                    tempWithNoEps2.put(productionArray[i+1], new HashSet<>(Collections.emptyList()));
                                                    tempWithNoEps2.get(productionArray[i+1]).addAll(xFY);
                                                  follow.get(productionArray[i]).addAll(tempWithNoEps2.get(productionArray[i + 1]));
                                			 
                                			
                                		 }
                            
                            		 }
                            	 }
                             }
                        		 
                             }
                             for(int i = 0;i<productionArray.length-1;i++) {
                                 if (first.get(productionArray[i + 1]).contains(EPSILON)) {
    	                        	 if (!isSubset(follow.get(entry.getKey()), follow.get(productionArray[i]))) {
    	                                 follow.get(productionArray[i]).addAll(follow.get(entry.getKey()));
    	                                 change = true;
    	
    	                             }
    	                        	if(i==0&&i+2<productionArray.length) {
    	                        		 follow.get(productionArray[i]).addAll(first.get(productionArray[i + 2]));
    	                        	}
                                 }
                                 
                             }
                            
                         }
                         else {
                             follow.get(productionArray[0]).addAll(follow.get(entry.getKey()));
                         }
                       
            	     }
            	 }
             }
             followFinal =  new LinkedHashMap<>(follow);
             return encodeFollow(follow);
        }
        public  String encodeFirst(Map<String, Set<String>> first) {
        	ArrayList<String> terms = new ArrayList<String>();
        	for(Map.Entry<String, ArrayList<String>> entry : this.rules.entrySet()){
        	terms.add(entry.getKey());
        	}
            String overall = "";
            boolean startVar = false;
            String start = "";
            for (String v : terms) {
            	if(v.equals("S")) {
            		startVar = true;
            	}
            	String part = "";
            	Object[] firstString= first.get(v).toArray();
            	for(int i = 0 ;i<firstString.length;i++) {
            		if(firstString[i].equals("$")) {
            			part+="e";
//            			System.out.print("e");
            		}
            		else {
            			part+=firstString[i];
//                		System.out.print(firstString[i]);
            		}
            	}
                char[] charArray = part.toCharArray();
                //4
                for (int i = 0; i < charArray.length; i++) {
                    for (int j = i + 1; j < charArray.length; j++) {
                        if (Character.toLowerCase(charArray[j]) < Character.toLowerCase(charArray[i])) {
                            swapChars(i, j, charArray);
                        }
                    }
                }
                part = String.valueOf(charArray);
            	part = v+","+part+";";
            	if(!startVar) {
                	overall+=part;
            	}
            	else {
            		start+=part;
            	}
            
                
            }
            overall = start + overall;
        	return overall;

        }
        private static void swapChars(int i, int j, char[] charArray) {
            char temp = charArray[i];
            charArray[i] = charArray[j];
            charArray[j] = temp;
        }
        public  String encodeFollow(Map<String, Set<String>> follow) {
        	ArrayList<String> terms = new ArrayList<String>();
        	for(Map.Entry<String, ArrayList<String>> entry : this.rules.entrySet()){
        	terms.add(entry.getKey());
        	}
            String overall = "";
            boolean startVar = false;
            String start = "";
            for (String v : terms) {
            	if(v.equals("S")) {
            		startVar = true;
            	}
          	  boolean eps = false;
            	String part = "";
            	Object[] firstString= follow.get(v).toArray();
            	for(int i = 0 ;i<firstString.length;i++) {
            		if(firstString[i].equals("$")) {
            			eps = true;
//            			System.out.print("e");
            		}
            		else {
            			part+=firstString[i];
//                		System.out.print(firstString[i]);
            		}
            	}
                char[] charArray = part.toCharArray();
                //4
                for (int i = 0; i < charArray.length; i++) {
                    for (int j = i + 1; j < charArray.length; j++) {
                        if (Character.toLowerCase(charArray[j]) < Character.toLowerCase(charArray[i])) {
                            swapChars(i, j, charArray);
                        }
                    }
                }
                part = String.valueOf(charArray);
                if(!startVar) {
                	 if(eps) {
                      	 part = v+","+part+"$"+";";
                        }
                        else {
                          part = v+","+part+";";
                        }
                        overall+=part;
                }
                else {
                	 if(eps) {
                      	 part = v+","+part+"$"+";";
                        }
                        else {
                          part = v+","+part+";";
                        }
                        start+=part;
                }
               
            
                
            }
            overall = start + overall;
        	return overall;

      }
    }

}
